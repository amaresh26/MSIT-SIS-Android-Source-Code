package in.msitprogram.quickmark.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.models.CourseModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 24/05/17.
 */

public class StudentMarksActivity extends BaseActivity {

    private AppCompatEditText inputStudentName;
    private AppCompatEditText inputCourseName;
    private TextView fromDate;
    private TextView percentageTv;
    private TextView toDate;
    private TextView studentRemarks;
    private CourseModel mCourseModel;
    private LinearLayout mParent;
    private TextView mRecordNotFound;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marks);
        //setting the tool bar
        setToolbar("Marks");
        //finding the view
        findViews();
        //getting from the intent (Bundle)
        getBundleData(savedInstanceState);
        //calling the api for percentage
        studentMarksApi();
        //closing the keypad
        closeKeypad();

    }

    /*
    * calling the student marks api
    * @param1 guid
    * @param2 course_id ie cid
    * */
    private void studentMarksApi() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());
        mParam.put("cid", mCourseModel.getCourse_id());

        new VolleyTask(StudentMarksActivity.this, Constants.GET_STUDENT_MARKS, mParam, "Please wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObject = new JSONObject(response);
                    if (mMainObject.getString("result").equals("success")) {
                        if (mMainObject.isNull("data")) {
                            mParent.setVisibility(View.GONE);
                            mRecordNotFound.setVisibility(View.VISIBLE);
                        } else {
                            JSONObject data = mMainObject.getJSONObject("data");
                            String percentage = data.getString("percentage");
                            String started_date = data.getString("started_date");
                            String ended_date = data.getString("ended_date");
                            String remarks = data.getString("remarks");

                            //setting the data which is from api
                            setFieldData(percentage, started_date, ended_date,remarks);
                        }
                    } else {
                        makeToast(mMainObject.getString("msg"), LONG);
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
    * setting the data which we got it from api, session and course model
    * */
    private void setFieldData(String percentage, String started_date, String ended_date,String remarks) {
        inputStudentName.setText(mSessionManager.getFullName());
        inputStudentName.setEnabled(FALSE);
        inputCourseName.setText(mCourseModel.getCourse_name());
        inputCourseName.setEnabled(FALSE);
        fromDate.setText(started_date);
        toDate.setText(ended_date);
        percentageTv.setText(percentage + "%");
        studentRemarks.setText(remarks);
    }

    /*
    * this is to get the data from the course activity through the
    * intent and saving the instance to the CourseModel
    * */
    private void getBundleData(Bundle savedInstanceState) {
        Intent extra = getIntent();
        if (savedInstanceState == null) {
            if (extra == null) {
                mCourseModel = null;
            } else {
                mCourseModel = (CourseModel) extra.getSerializableExtra("CourseModel");
            }
        } else {
            mCourseModel = (CourseModel) extra.getSerializableExtra("CourseModel");
        }
    }


    /**
     * Find the Views in the layout
     */
    private void findViews() {
        studentRemarks = (TextView) findViewById(R.id.student_remark);
        inputStudentName = (AppCompatEditText) findViewById(R.id.input_student_name);
        inputCourseName = (AppCompatEditText) findViewById(R.id.input_course_name);
        fromDate = (TextView) findViewById(R.id.from_date);
        toDate = (TextView) findViewById(R.id.to_date);
        percentageTv = (TextView) findViewById(R.id.percentage);
        mParent = (LinearLayout) findViewById(R.id.parent_layout);
        mRecordNotFound = (TextView) findViewById(R.id.no_data);

    }
}
