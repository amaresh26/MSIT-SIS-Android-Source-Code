package in.msitprogram.quickmark.activities.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.adapter.EventDetailsListAdapter;
import in.msitprogram.quickmark.models.EventDetailsModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 24/05/17.
 */

public class StudentEventListActivity extends BaseActivity {

    private ListView mEventListView;
    private ArrayList<EventDetailsModel> mEventList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event_list);
        //setting the tool bar
        setToolbar("Event List");
        //arraylist
        mEventList = new ArrayList<EventDetailsModel>();
        //finding view
        mEventListView = (ListView) findViewById(R.id.event_list);
        //getting the data from the api and showing in the list
        getEventsList();

        //on item selected from the list view
        mEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventDetailsModel mEventDetailsModel = mEventList.get(position);
                HashMap mObject = new HashMap();
                mObject.put("EventModel", mEventDetailsModel);
                //sending the event model object to another activity
                nextIntent(StudentEventDetailsActivity.class, mObject, FALSE);
            }
        });
    }

    /*
    * calling the api for the event data
    * @param1 guid
    * @param2 event_date
    * */
    private void getEventsList() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());
        mParam.put("event_date", mobileDate());

        new VolleyTask(StudentEventListActivity.this, Constants.GET_EVENT_LIST, mParam, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        JSONArray mData = mMainObj.getJSONArray("data");
                        for (int i = 0; i < mData.length(); i++) {
                            JSONObject eventData = mData.getJSONObject(i);
                            String title = eventData.getString("title");
                            String bid = eventData.getString("bid");
                            String description = eventData.getString("description");
                            String event_date = eventData.getString("event_date");
                            String created_by = eventData.getString("created_by");
                            //setting the data to the model
                            EventDetailsModel mEventDetailsModel = new EventDetailsModel(bid, title, created_by, description, event_date);
                            //adding the model objects to array list
                            mEventList.add(mEventDetailsModel);
                        }
                        EventDetailsListAdapter mEventDetailsListAdapter = new EventDetailsListAdapter(mEventList, StudentEventListActivity.this);
                        mEventListView.setAdapter(mEventDetailsListAdapter);
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
    * getting the date form the system
    * date will be the mobile date
    * */
    public String mobileDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(StudentEventListActivity.this)) {
            showNoNetworkDialogue(StudentEventListActivity.this);
        }
    }
}
