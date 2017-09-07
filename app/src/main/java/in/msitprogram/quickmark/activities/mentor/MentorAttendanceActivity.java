package in.msitprogram.quickmark.activities.mentor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.adapter.AttendanceStudentAdapter;
import in.msitprogram.quickmark.models.StudentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 19/05/17.
 */

public class MentorAttendanceActivity extends BaseActivity {

    private ListView studentLv;
    private ArrayList<StudentDetailsModel> studentModelList;
    private AttendanceStudentAdapter mStudentListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        //setting the toolbar
        setToolbar("Attendance");
        studentLv = (ListView) findViewById(R.id.student_list);
        studentModelList = new ArrayList();
        //getting the list of students from the api
        getStudentList();

//        studentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MentorAttendanceActivity.this);
//                builderSingle.setTitle("Select One Option");
//
//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MentorAttendanceActivity.this, android.R.layout.select_dialog_item);
//                arrayAdapter.add("Absent");
//                arrayAdapter.add("Half Day");
//                arrayAdapter.add("Late");
//                //arrayAdapter.add("Present");
//
//                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String strName = arrayAdapter.getItem(which);
//                        if (strName.equals("Absent")) {
//                            studentModelList.get(position).setStudentAttendance("0");
//                        } else if (strName.equals("Half Day")) {
//                            studentModelList.get(position).setStudentAttendance("2");
//                        } else if (strName.equals("Late")) {
//                            studentModelList.get(position).setStudentAttendance("1");
//                        } else if (strName.equals("Present")) {
//                            studentModelList.get(position).setStudentAttendance("3");
//                        }
//                        mStudentListAdapter.notifyDataSetChanged();
//                    }
//                });
//                builderSingle.show();
//            }
//        });
    }

    /*
    * getting the student list by volley task form Api
    * we will get the User id, User Name, User Roll Number
    * User Image. Set an adapter to the list after successful
    * */
    private void getStudentList() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());

        new VolleyTask(MentorAttendanceActivity.this, Constants.GET_STUDENT_BY_COURSE_ENROLLED, mParam, "Please wait...") {
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
                            String attendance_type = studentDetails.getString("attendance_type");
                            //setting the data to the model
                            StudentDetailsModel mStudentDetailsModel = new StudentDetailsModel(studentFullName,
                                    studentRollNumber, studentUserImg, studentMobile, studentEmail, attendance_type, student_id);
                            //adding the model data to the array list
                            studentModelList.add(mStudentDetailsModel);
                        }
                        //setting the student adapter to the list
                        mStudentListAdapter = new AttendanceStudentAdapter(MentorAttendanceActivity.this, studentModelList);
                        studentLv.setAdapter(mStudentListAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_attendance, menu);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance, menu);

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
                    studentLv.clearTextFilter();
                } else {
                    mStudentListAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.student_attendance) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want confirm attendance?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    String studentAttendanceStr = getStudentAttendance();
                    //send attendance data to server
                    sendStudentAttendance(studentAttendanceStr);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        return true;
    }

    /*
    * api call for attendance
    * */
    private void sendStudentAttendance(String studentAttendanceStr) {

        HashMap<String, String> mParams = new HashMap<>();
        mParams.put("guid", mSessionManager.getGUID());
        mParams.put("date", "2017-06-04");
        mParams.put("attendance", studentAttendanceStr);

        new VolleyTask(MentorAttendanceActivity.this, Constants.STUDENT_ATTENDANCE, mParams, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        makeToast("Attendance Has been taken", LONG);
                        finish();
                    } else {
                        makeToast(mMainObj.getString("msg"), LONG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    /*
    * to get the data of the students from list view
    * and there attendance and convert into json string
    * */
    private String getStudentAttendance() {
        JSONArray mJsonArray = new JSONArray();

        for (int i = 0; i < mStudentListAdapter.getCount(); ++i) {
            StudentDetailsModel model = (StudentDetailsModel) mStudentListAdapter.getItem(i);
            Log.e("model", model.getStudentAttendance());
            if (!model.getStudentAttendance().equals("3")) {
                JSONObject mJsonObject = new JSONObject();
                try {
                    mJsonObject.put("uid", model.getUid());
                    mJsonObject.put("status", model.getStudentAttendance());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mJsonArray.put(mJsonObject);
            }
        }
        return mJsonArray.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(MentorAttendanceActivity.this)) {
            showNoNetworkDialogue(MentorAttendanceActivity.this);
        }
    }

}