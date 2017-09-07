package in.msitprogram.quickmark.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import in.msitprogram.quickmark.adapter.StudentListAdapter;
import in.msitprogram.quickmark.models.CourseModel;
import in.msitprogram.quickmark.models.StudentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by Amaresh jana on 31/05/17.
 */

public class StudentMyTeamActivity extends BaseActivity {

    private ListView mStudentList;
    private ArrayList<StudentDetailsModel> studentList;
    private String cid;
    private TextView mNoData;
    private StudentListAdapter mStudentListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        setToolbar("Friends List");
        mStudentList = (ListView) findViewById(R.id.student_list);
        mNoData = (TextView) findViewById(R.id.no_data_friend_list);
        studentList = new ArrayList<>();
        //geting course id form intent
        getBundleData(savedInstanceState);
        //calling student list api
        getAllStudentDetails();
    }

    /*
    * this is to get the data from the course activity through the
    * intent and saving the instance to the CourseModel
    * */
    private void getBundleData(Bundle savedInstanceState) {
        Intent extra = getIntent();
        CourseModel mCourseModel;
        if (savedInstanceState == null) {
            if (extra == null) {
                mCourseModel = null;
            } else {
                mCourseModel = (CourseModel) extra.getSerializableExtra("CourseModel");
                cid = mCourseModel.getCourse_id();
            }
        } else {
            mCourseModel = (CourseModel) extra.getSerializableExtra("CourseModel");
            cid = mCourseModel.getCourse_id();
        }
    }


    private void getAllStudentDetails() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());
        mParam.put("cid", cid);

        new VolleyTask(StudentMyTeamActivity.this, Constants.GET_MY_TEAM_DATA, mParam, "Please wait...") {
            @Override
            protected void handleError(VolleyError error) {
                mStudentList.setVisibility(View.GONE);
                mNoData.setVisibility(View.VISIBLE);
            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObject = new JSONObject(response);
                    if (mMainObject.getString("result").equals("success")) {
                        if (mMainObject.isNull("data")) {
                            mStudentList.setVisibility(View.GONE);
                            mNoData.setVisibility(View.VISIBLE);
                        } else {
                            JSONArray mData = mMainObject.getJSONArray("data");
                            for (int i = 0; i < mData.length(); i++) {
                                JSONObject studentDetails = mData.getJSONObject(i);
                                String studentFullName = studentDetails.getString("full_name");
                                String studentMobile = studentDetails.getString("mobile");
                                String studentEmail = studentDetails.getString("email");
                                String studentUserImg = studentDetails.getString("user_img");
                                String studentRollNumber = studentDetails.getString("roll_number");
                                //setting the data to the model
                                StudentDetailsModel mStudentDetailsModel = new StudentDetailsModel(studentFullName,
                                        studentRollNumber, studentUserImg, studentMobile, studentEmail, "3", "");
                                //adding the model data to the array list
                                studentList.add(mStudentDetailsModel);
                            }
                            //setting the student adapter to the list
                            mStudentListAdapter = new StudentListAdapter(StudentMyTeamActivity.this, studentList);
                            mStudentList.setAdapter(mStudentListAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mStudentList.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    mStudentList.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(StudentMyTeamActivity.this)) {
            showNoNetworkDialogue(StudentMyTeamActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_details_in_mentor, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mStudentListAdapter.getFilter().filter("");
                    mStudentList.clearTextFilter();
                } else {
                    mStudentListAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }
}
