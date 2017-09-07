package in.msitprogram.quickmark.activities.mentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.adapter.StudentMarksAdapter;
import in.msitprogram.quickmark.models.StudentMarksAllCourseModel;
import in.msitprogram.quickmark.models.StudentParentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 03/06/17.
 */

public class StudentDetailedMarksActivity extends BaseActivity {
    //url GET_STUDENT_ALL_COURSE_MARKS

    private StudentParentDetailsModel mStudentModel;
    private ImageView studentImage;
    private TextView studentFullNameList;
    private TextView studentRollNoList;
    private TextView studentMobileNo;
    private TextView studentEmail;
    private TextView parentName;
    private TextView parentMobile;
    private TextView parentEmail;
    private ListView studentDetailedMarks;
    private ArrayList<StudentMarksAllCourseModel> mStudentPercentage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detailed_marks);
        setToolbar("Student Detailed Marks");
        //finding the view
        findViews();
        //inistialing the mStudentPercentage
        mStudentPercentage = new ArrayList();
        //getting from the intent (Bundle)
        getBundleData(savedInstanceState);
        //get student marks of all courses calling api
        getStudentMarksAllSubject();
        //setdata of the student details
        setDataOfStudent();
    }

    private void setDataOfStudent() {
        if (!mStudentModel.getStudentImageUrl().equals("")) {
            Picasso.with(mContext).load(Constants.IMAGE_BASE_URL+mStudentModel.getStudentImageUrl()).fit().centerCrop().into(studentImage);
        } else {
            studentImage.setImageResource(R.mipmap.ic_launcher);
        }
        studentFullNameList.setText(mStudentModel.getFullName());
        studentRollNoList.setText(mStudentModel.getRollNumber());
        studentMobileNo.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        studentMobileNo.setLinksClickable(true);
        studentMobileNo.setText(mStudentModel.getStudentMobileNo());
        studentEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        studentEmail.setLinksClickable(true);
        studentEmail.setText(mStudentModel.getStudentEmail());
        parentName.setText(mStudentModel.getParentName());
        parentMobile.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        parentMobile.setLinksClickable(true);
        parentMobile.setText(mStudentModel.getParentMobile());
        parentEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        parentEmail.setLinksClickable(true);
        parentEmail.setText(mStudentModel.getParentEmail());
    }

    /*
    * this is to call the api for the student all courses marks
    * and display in a list view
    * */
    private void getStudentMarksAllSubject() {
        HashMap<String, String> mParams = new HashMap();
        mParams.put("guid", mSessionManager.getGUID());
        mParams.put("student_id", mStudentModel.getUid());

        new VolleyTask(StudentDetailedMarksActivity.this, Constants.GET_STUDENT_ALL_COURSE_MARKS, mParams, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        JSONArray mDataArray = mMainObj.getJSONArray("data");
                        for (int i = 0; i < mDataArray.length(); ++i) {
                            JSONObject mStudent = mDataArray.getJSONObject(i);
                            String courseName = mStudent.getString("course_name");
                            String startDate = mStudent.getString("started_date");
                            String fromDate = mStudent.getString("ended_date");
                            String percentage = mStudent.getString("percentage");

                            StudentMarksAllCourseModel marksAllCourseModel = new StudentMarksAllCourseModel(courseName, percentage, startDate, fromDate);
                            mStudentPercentage.add(marksAllCourseModel);
                        }
                        StudentMarksAdapter mStudentMarksAdapter = new StudentMarksAdapter(mStudentPercentage, StudentDetailedMarksActivity.this);
                        studentDetailedMarks.setAdapter(mStudentMarksAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Find the Views in the layout<br />
     */
    private void findViews() {
        studentImage = (ImageView) findViewById(R.id.student_image);
        studentFullNameList = (TextView) findViewById(R.id.student_full_name_list);
        studentRollNoList = (TextView) findViewById(R.id.student_roll_no_list);
        studentMobileNo = (TextView) findViewById(R.id.student_mobile_no);
        studentEmail = (TextView) findViewById(R.id.student_email);
        parentName = (TextView) findViewById(R.id.parent_name);
        parentMobile = (TextView) findViewById(R.id.parent_mobile);
        parentEmail = (TextView) findViewById(R.id.parent_email);
        studentDetailedMarks = (ListView) findViewById(R.id.student_detailed_marks);
    }

    /*
    * this is to get the data from the course activity through the
    * intent and saving the instance to the CourseModel
    * */
    private void getBundleData(Bundle savedInstanceState) {
        Intent extra = getIntent();
        if (savedInstanceState == null) {
            if (extra == null) {
                mStudentModel = null;
            } else {
                mStudentModel = (StudentParentDetailsModel) extra.getSerializableExtra("Student_marks");
                //(mStudentModel.getStudentImageUrl(),LONG);
            }
        } else {
            mStudentModel = (StudentParentDetailsModel) extra.getSerializableExtra("Student_marks");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(StudentDetailedMarksActivity.this)) {
            showNoNetworkDialogue(StudentDetailedMarksActivity.this);
        }
    }
}