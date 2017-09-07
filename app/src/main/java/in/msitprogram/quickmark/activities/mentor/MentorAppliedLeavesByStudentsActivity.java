package in.msitprogram.quickmark.activities.mentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.adapter.LeaveStatusAdapter;
import in.msitprogram.quickmark.models.LeaveStatusModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 06/06/17.
 */

public class MentorAppliedLeavesByStudentsActivity extends BaseActivity {

    private ListView mStudentLeaveHistory;
    private ArrayList<LeaveStatusModel> mLeaveStatusList;
    private String nav;
    private TextView studentNameTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_leave_history);
        setToolbar("Leave Status");
        studentNameTv  = (TextView) findViewById(R.id.student_name_tv);
        studentNameTv.setText("Student Name");
        mStudentLeaveHistory = (ListView) findViewById(R.id.student_leave_history_lv);
        //list of models objects in the list
        mLeaveStatusList = new ArrayList<>();
        //calling the api for leave history
        getStudentLeaveHistory();
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

        new VolleyTask(MentorAppliedLeavesByStudentsActivity.this, Constants.GET_LEAVES_APPLIED, mParams, "Please Wait...") {
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
                            String studentName = mData.getString("full_name");

                            LeaveStatusModel mModel = new LeaveStatusModel(mReason, from_date, to_date, lid, status, cid,studentName);
                            mLeaveStatusList.add(mModel);
                        }
                        LeaveStatusAdapter mLeaveStatusAdapter = new LeaveStatusAdapter(mLeaveStatusList, MentorAppliedLeavesByStudentsActivity.this);
                        mStudentLeaveHistory.setAdapter(mLeaveStatusAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
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
        if (!Networking.isNetworkAvailable(MentorAppliedLeavesByStudentsActivity.this)){
            showNoNetworkDialogue(MentorAppliedLeavesByStudentsActivity.this);
        }
    }
}
