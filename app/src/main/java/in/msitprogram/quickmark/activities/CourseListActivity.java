package in.msitprogram.quickmark.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import in.msitprogram.quickmark.activities.mentor.MentorMyTeamActivity;
import in.msitprogram.quickmark.activities.student.StudentMarksActivity;
import in.msitprogram.quickmark.activities.student.StudentMyTeamActivity;
import in.msitprogram.quickmark.adapter.CourseListAdapter;
import in.msitprogram.quickmark.models.CourseModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 19/05/17.
 */
public class CourseListActivity extends BaseActivity {

    private ArrayList<CourseModel> courseList;
    private ListView mCourseListView;
    private String navFrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        //setting the tool bar
        setToolbar("Course List");
        //finding the view
        mCourseListView = (ListView) findViewById(R.id.course_list);
        //array list to store the course model objects it is of generic type course model
        courseList = new ArrayList<>();
        //getting the course list from api call
        getCourseList();
        //getDataFromTheIntent
        getDataFromIntent(savedInstanceState);

        //clicking on the list
        mCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseModel mCourseModel = courseList.get(position);
                HashMap<Object, Object> mObject = new HashMap<>();
                mObject.put("CourseModel", mCourseModel);
                //sending the data in intent by the intent coming from the nav mentor
                if (navFrom == null){
                    nextIntent(StudentMarksActivity.class, mObject, FALSE);
                }else if (navFrom.equals("MY_TEAM")){
                    nextIntent(StudentMyTeamActivity.class, mObject, FALSE);
                }else if (navFrom.equals("MY_TEAM_MENTORS")){
                    nextIntent(MentorMyTeamActivity.class, mObject, FALSE);
                }
            }
        });
    }

    /*
    * get the data from mentor nav and navigating to the different activity
    * */
    private void getDataFromIntent(Bundle savedInstanceState) {
        Intent extra = getIntent();
        if (savedInstanceState == null) {
            if (extra == null) {
                navFrom = null;
            } else {
                navFrom = (String) extra.getSerializableExtra("NAV");
            }
        } else {
            navFrom = (String) extra.getSerializableExtra("NAV");
        }
    }

    /*
    * getting the course list from api by volley
    * course_id, course_name, course duration
    * */
    private void getCourseList() {
        HashMap<String, String> courseParams = new HashMap<>();
        courseParams.put("guid", mSessionManager.getGUID());

        new VolleyTask(CourseListActivity.this, Constants.COURSE_URL, courseParams, "Getting Course Details...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                Log.e("Course List Response", response);
                try {
                    JSONObject parentObj = new JSONObject(response);
                    if (parentObj.getString("result").equals("success")) {
                        JSONArray dataArray = parentObj.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject courseData = dataArray.getJSONObject(i);
                            String cid = courseData.getString("cid");
                            String course_name = courseData.getString("course_name");
                            String no_of_leaves = courseData.getString("no_of_leaves");
                            String from_date = courseData.getString("from_date");
                            String to_date = courseData.getString("to_date");

                            //inserting the data into model
                            CourseModel mCourseModel = new CourseModel(cid, course_name, no_of_leaves, from_date, to_date);
                            //adding the course model object in to the ArrayList
                            courseList.add(mCourseModel);
                        }
                        //setting the adapter to the listview
                        CourseListAdapter mCourseListAdapter = new CourseListAdapter(CourseListActivity.this, courseList);
                        mCourseListView.setAdapter(mCourseListAdapter);
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
        if (!Networking.isNetworkAvailable(CourseListActivity.this)) {
            showNoNetworkDialogue(CourseListActivity.this);
        }
    }
}