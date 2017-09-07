package in.msitprogram.quickmark.activities.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import in.msitprogram.quickmark.models.StudentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 24/05/17.
 */

public class StudentDetailsActivity extends BaseActivity {

    private ListView mStudentList;
    private ArrayList<StudentDetailsModel> studentList;
    private TextView mNoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        setToolbar("Friends List");
        mStudentList = (ListView) findViewById(R.id.student_list);
        mNoData = (TextView) findViewById(R.id.no_data_friend_list);
        studentList = new ArrayList<>();

        //calling student list api
        getAllStudentDetails();
    }

    private void getAllStudentDetails() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());

        new VolleyTask(StudentDetailsActivity.this, Constants.GET_STUDENT_DETAILS, mParam, "Please wait...") {
            @Override
            protected void handleError(VolleyError error) {

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
                                String student_id = studentDetails.getString("uid");
                                //setting the data to the model
                                StudentDetailsModel mStudentDetailsModel = new StudentDetailsModel(studentFullName,
                                        studentRollNumber, studentUserImg, studentMobile, studentEmail, "3", student_id);
                                //adding the model data to the array list
                                studentList.add(mStudentDetailsModel);
                            }
                            //setting the student adapter to the list
                            StudentListAdapter mStudentListAdapter = new StudentListAdapter(StudentDetailsActivity.this, studentList);
                            mStudentList.setAdapter(mStudentListAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(StudentDetailsActivity.this)) {
            showNoNetworkDialogue(StudentDetailsActivity.this);
        }
    }
}
