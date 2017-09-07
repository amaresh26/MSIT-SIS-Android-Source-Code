package in.msitprogram.quickmark.activities.mentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import in.msitprogram.quickmark.adapter.MentorStudentListAdapter;
import in.msitprogram.quickmark.models.StudentParentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 05/06/17.
 */

public class StudentDetailsInMentorActivity extends BaseActivity {
    private ListView mStudentList;
    private ArrayList<StudentParentDetailsModel> studentModelList;
    private MentorStudentListAdapter mStudentListAdapter;
    private String nav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_myteam);
        setToolbar("Student Details");//finding view
        mStudentList = (ListView) findViewById(R.id.mentor_myteams);
        studentModelList = new ArrayList();
        //getting from the intent (Bundle)
        getBundleData(savedInstanceState);
        //calling api for the data
        getStudentList();

        mStudentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (nav != null) {
                    StudentParentDetailsModel studentParentDetailsModel = studentModelList.get(position);
                    HashMap<Object, Object> mParams = new HashMap();
                    mParams.put("Student_marks", studentParentDetailsModel);
                    nextIntent(StudentDetailedMarksActivity.class, mParams, FALSE);
                }
            }
        });
    }


    /*
    * getting the student list by volley task form Api
    * we will get the User id, User Name, User Roll Number
    * User Image. Set an adapter to the list after successful
    * */
    private void getStudentList() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());

        new VolleyTask(StudentDetailsInMentorActivity.this, Constants.GET_STUDENT_DETAILS, mParam, "Please wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObject = new JSONObject(response);
                    if (mMainObject.getString("result").equals("success")) {
                        JSONArray mData = mMainObject.getJSONArray("data");
                        for (int i = 0; i < mData.length(); i++) {
                            JSONObject studentDetails = mData.getJSONObject(i);
                            String studentFullName = studentDetails.getString("full_name");
                            String studentMobile = studentDetails.getString("mobile");
                            String studentEmail = studentDetails.getString("email");
                            String studentUserImg = studentDetails.getString("user_img");
                            String studentRollNumber = studentDetails.getString("roll_number");
                            String student_id = studentDetails.getString("uid");
                            String parent_name = studentDetails.getString("parent_name");
                            String parent_mobile = studentDetails.getString("parent_mobile");
                            String parent_email = studentDetails.getString("parent_email");
                            StudentParentDetailsModel mStudentParentDetailsModel = new StudentParentDetailsModel(studentFullName, studentRollNumber, studentUserImg, studentMobile,
                                    studentEmail, parent_name, parent_mobile, parent_email, student_id);
                            //adding the model data to the array list
                            studentModelList.add(mStudentParentDetailsModel);
                        }
                        //setting the student adapter to the list
                        mStudentListAdapter = new MentorStudentListAdapter(StudentDetailsInMentorActivity.this, studentModelList);
                        mStudentList.setAdapter(mStudentListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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
        if (!Networking.isNetworkAvailable(StudentDetailsInMentorActivity.this)) {
            showNoNetworkDialogue(StudentDetailsInMentorActivity.this);
        }
    }
}
