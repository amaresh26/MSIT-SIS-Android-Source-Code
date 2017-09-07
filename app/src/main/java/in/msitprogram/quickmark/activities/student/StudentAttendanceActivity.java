package in.msitprogram.quickmark.activities.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
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
import in.msitprogram.quickmark.adapter.StudentAttendanceAdapter;
import in.msitprogram.quickmark.models.StudentAttendanceModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 19/05/17.
 */

public class StudentAttendanceActivity extends BaseActivity {

    private AppCompatEditText inputStudentName;
    private TextView totalLeaves;
    private TextView leavesTaken;
    private TextView leavesRemaining;
    private ListView studentAttendanceList;
    private String mLeaveTaken;
    private ArrayList<StudentAttendanceModel> mStudentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        setToolbar("Attendance");
        //finding the views
        findViews();
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        inputStudentName = (AppCompatEditText) findViewById(R.id.input_student_name);
        inputStudentName.setEnabled(FALSE);
        totalLeaves = (TextView) findViewById(R.id.total_leaves);
        leavesTaken = (TextView) findViewById(R.id.leaves_taken);
        leavesRemaining = (TextView) findViewById(R.id.leaves_remaining);
        studentAttendanceList = (ListView) findViewById(R.id.student_attendance_list);
        mStudentList = new ArrayList<>();
        //method in base activity to close keypad
        closeKeypad();
        //getting the attendance from the api
        getStudentAttendance();

    }

    private void setDataFromApi() {
        inputStudentName.setText(mSessionManager.getFullName());
        totalLeaves.setText("12");
        leavesTaken.setText(mLeaveTaken);
        leavesRemaining.setText(12 - Double.parseDouble(mLeaveTaken) + "");
    }

    /*
    * this method is for getting the data from the api and showing
    *the details of student attendance in the activity
    * */
    private void getStudentAttendance() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());

        new VolleyTask(StudentAttendanceActivity.this, Constants.GET_STUDENT_ATTENDANCE, mParam, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        mLeaveTaken = mMainObj.getString("total_taken_leaves");
                        if (!mMainObj.getString("msg").contains("No")) {
                            JSONArray mData = mMainObj.getJSONArray("data");
                            if (mData.length() > 0) {
                                for (int i = 0; i < mData.length(); ++i) {
                                    JSONObject mAttendance = mData.getJSONObject(i);
                                    String course_name = mAttendance.getString("course_name");
                                    String taken_leave_on = mAttendance.getString("taken_leave_on");
                                    String attendance_type_name = mAttendance.getString("attendance_type_name");

                                    StudentAttendanceModel mStudentAttendanceModel = new StudentAttendanceModel(course_name, taken_leave_on, attendance_type_name);
                                    mStudentList.add(mStudentAttendanceModel);
                                }
                                StudentAttendanceAdapter mStudentAttendanceAdapter = new StudentAttendanceAdapter(mStudentList, StudentAttendanceActivity.this);
                                studentAttendanceList.setAdapter(mStudentAttendanceAdapter);
                            }
                        }
                        //setData whcih we obtained from the api
                        setDataFromApi();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(StudentAttendanceActivity.this)) {
            showNoNetworkDialogue(StudentAttendanceActivity.this);
        }
    }
}
