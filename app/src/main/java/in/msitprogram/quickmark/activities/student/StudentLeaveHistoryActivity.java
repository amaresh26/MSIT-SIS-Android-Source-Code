package in.msitprogram.quickmark.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.activities.mentor.MentorApproveLeaveActivity;
import in.msitprogram.quickmark.adapter.LeaveStatusAdapter;
import in.msitprogram.quickmark.models.LeaveStatusModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 29/05/17.
 */

public class StudentLeaveHistoryActivity extends BaseActivity {

    private ListView mStudentLeaveHistory;
    private ArrayList<LeaveStatusModel> mLeaveStatusList;
    private String nav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_leave_history);
        setToolbar("Leave Status");
        mStudentLeaveHistory = (ListView) findViewById(R.id.student_leave_history_lv);
        //list of models objects in the list
        mLeaveStatusList = new ArrayList<>();

        //getting from the intent (Bundle)
        getBundleData(savedInstanceState);
        mStudentLeaveHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (nav != null) {
                    LeaveStatusModel LeaveStatusModel = mLeaveStatusList.get(position);
                    HashMap<Object, Object> mParams = new HashMap();
                    mParams.put("Leave_approve", LeaveStatusModel);
                    nextIntent(MentorApproveLeaveActivity.class, mParams, FALSE);
                }
            }
        });
    }

    /*
        * CALLING the api for getting the leaves applied and status of the leaves
        * */
    private void getStudentLeaveHistory() {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put("guid", mSessionManager.getGUID());

        new VolleyTask(StudentLeaveHistoryActivity.this, Constants.GET_STUDENT_LEAVE_HISTORY_URL, mParams, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        JSONArray mMainArray = mMainObj.getJSONArray("data");
                        for (int i = 0; i < mMainArray.length(); ++i) {
                            JSONObject mData = mMainArray.getJSONObject(i);
                            String mReason = mData.getString("reason");
                            String lid = mData.getString("lid");
                            String from_date = mData.getString("from_date");
                            String to_date = mData.getString("to_date");
                            String status = mData.getString("status");
                            String cid = mData.getString("cid");
                            String studentName = "";

                            LeaveStatusModel mModel = new LeaveStatusModel(mReason, from_date, to_date, lid, status, cid,studentName);
                            mLeaveStatusList.add(mModel);
                        }
                        LeaveStatusAdapter mLeaveStatusAdapter = new LeaveStatusAdapter(mLeaveStatusList, StudentLeaveHistoryActivity.this);
                        mStudentLeaveHistory.setAdapter(mLeaveStatusAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.apply_leave_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.apply_leave) {
            nextIntent(ApplyLeaveActivity.class, null, FALSE);
        }
        return true;
    }

    /*
    * this is to get the data from the course activity through the
    * intent and saving the instance to the CourseModel
    * */
    private void getBundleData(Bundle savedInstanceState) {
        Intent extra = getIntent();
        if (savedInstanceState == null) {
            if (extra == null) {
                nav = null;
            } else {
                nav = extra.getStringExtra("NAV");
            }
        } else
            nav = extra.getStringExtra("NAV");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(StudentLeaveHistoryActivity.this)) {
            showNoNetworkDialogue(StudentLeaveHistoryActivity.this);
        }else {
            //calling the api for leave history
            getStudentLeaveHistory();
        }
    }
}
