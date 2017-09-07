package in.msitprogram.quickmark.activities.mentor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import in.msitprogram.quickmark.adapter.MentorListAdapter;
import in.msitprogram.quickmark.models.MentorDetailsModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 24/05/17.
 */

public class MentorDetailsActivity extends BaseActivity {
    private ListView mMentorList;
    private ArrayList<MentorDetailsModel> mentorList;
    private MentorListAdapter mMentorListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_details);
        setToolbar("Mentor Details");
        mMentorList = (ListView) findViewById(R.id.mentor_list);
        mentorList = new ArrayList<>();

        //calling mentor list api
        getAllMentorDetails();
    }

    private void getAllMentorDetails() {
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());

        new VolleyTask(MentorDetailsActivity.this, Constants.GET_MENTOR_DETAILS, mParam, "Please wait...") {
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
                            JSONObject mentorDetails = mData.getJSONObject(i);
                            String mentorFullName = mentorDetails.getString("full_name");
                            String mentorMobile = mentorDetails.getString("mobile");
                            String mentorEmail = mentorDetails.getString("email");
                            String mentorUserImg = mentorDetails.getString("user_img");
                            String mentortypeId = mentorDetails.getString("user_type");
                            //setting the data to the model
                            MentorDetailsModel mMentorDetailsModel = new MentorDetailsModel(mentorFullName, mentorUserImg, mentorMobile, mentorEmail,mentortypeId);
                            //adding the model data to the array list
                            mentorList.add(mMentorDetailsModel);
                        }
                        //setting the mentor adapter to the list
                        mMentorListAdapter = new MentorListAdapter(MentorDetailsActivity.this, mentorList);
                        mMentorList.setAdapter(mMentorListAdapter);
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
        if (!Networking.isNetworkAvailable(MentorDetailsActivity.this)) {
            showNoNetworkDialogue(MentorDetailsActivity.this);
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
                    mMentorListAdapter.getFilter().filter("");
                    mMentorList.clearTextFilter();
                } else {
                    mMentorListAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }
}
