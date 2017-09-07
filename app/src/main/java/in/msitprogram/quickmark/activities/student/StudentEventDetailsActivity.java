package in.msitprogram.quickmark.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.TextView;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.models.EventDetailsModel;
import in.msitprogram.quickmark.utils.Networking;

/**
 * Created by amareshjana on 24/05/17.
 */

public class StudentEventDetailsActivity extends BaseActivity {

    private EventDetailsModel mEventDetailsModel;
    private AppCompatEditText title;
    private TextView description,postedBy;
    private Button eventDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event_details);
        //setting the tool bar
        setToolbar("Details");
        //finding the views
        findViews();
        //getting the event details from the event list in intent
        getBundleData(savedInstanceState);
        //settign the data which received form the event list from intent
        setEventDetails();
    }

    /*
    * this is to get the data from the course activity through the
    * intent and saving the instance to the CourseModel
    * */
    private void getBundleData(Bundle savedInstanceState) {
        Intent extra = getIntent();
        if (savedInstanceState == null) {
            if (extra == null) {
                mEventDetailsModel = null;
            } else {
                mEventDetailsModel = (EventDetailsModel) extra.getSerializableExtra("EventModel");
            }
        } else {
            mEventDetailsModel = (EventDetailsModel) extra.getSerializableExtra("EventModel");
        }
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        title = (AppCompatEditText) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        postedBy = (TextView) findViewById(R.id.posted_by);
        eventDate = (Button) findViewById(R.id.event_date);
    }

    private void setEventDetails() {
        title.setText(mEventDetailsModel.getTitle());
        title.setEnabled(FALSE);
        description.setText(mEventDetailsModel.getDescription());
        description.setEnabled(FALSE);
        eventDate.setText(mEventDetailsModel.getEventDate());
        eventDate.setEnabled(FALSE);
        postedBy.setText(mEventDetailsModel.getPostedBy());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(StudentEventDetailsActivity.this)) {
            showNoNetworkDialogue(StudentEventDetailsActivity.this);
        }
    }
}
