package in.msitprogram.quickmark.activities.mentor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 29/05/17.
 */

public class MentorPostEventActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout inputLayoutLeaveTitle;
    private AppCompatEditText title;
    private TextInputLayout inputLayoutLeaveReason;
    private AppCompatEditText description;
    private Button eventDate, submitPostEvent;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_post_event);
        setToolbar("Post Event");
        findViews();
    }

    /**
     * Find the Views in the layout<br />
     */
    private void findViews() {
        inputLayoutLeaveTitle = (TextInputLayout) findViewById(R.id.input_layout_leave_title);
        title = (AppCompatEditText) findViewById(R.id.title);
        inputLayoutLeaveReason = (TextInputLayout) findViewById(R.id.input_layout_leave_reason);
        description = (AppCompatEditText) findViewById(R.id.description);
        eventDate = (Button) findViewById(R.id.event_date);
        submitPostEvent = (Button) findViewById(R.id.submit_post_event);

        eventDate.setOnClickListener(this);
        submitPostEvent.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     */
    @Override
    public void onClick(View v) {
        if (v == eventDate) {
            //setting the date form the datepicker
            new DatePickerDialog(MentorPostEventActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v == submitPostEvent) {
            //post the event by calling the api
            postEvent();
        }
    }

    /*
    *this is to post the event by using api
    * */
    private void postEvent() {
        String mTitle = getData(title);
        String mDescription = getData(description);
        String mEventDate = getData(eventDate);
        String currentDateTimeString = "";
        try {
            currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            makeToast("Select the Event Date", LONG);
        }

        if (mTitle.equals("")) {
            inputLayoutLeaveTitle.setError("Enter the Title");
            title.requestFocus();
            return;
        } else if (mDescription.equals("")) {
            inputLayoutLeaveReason.setError("Enter the Description");
            description.requestFocus();
            return;
        } else if (mEventDate.equals("Event Date")) {
            makeToast("Select the Event Date", LONG);
            return;
        } else try {
            if (getDateFromString(mEventDate).compareTo(getDateFromString(currentDateTimeString)) < 0) {
                makeToast("Select Valid date", LONG);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            makeToast("Select the Event Date", LONG);
        }

        HashMap<String, String> mParams = new HashMap<>();
        mParams.put("guid", mSessionManager.getGUID());
        mParams.put("title", mTitle);
        mParams.put("desc", mDescription);
        mParams.put("event_date", mEventDate);
        new VolleyTask(MentorPostEventActivity.this, Constants.POST_EVENT_URL, mParams, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        makeToast("Event Posted Successfully", LONG);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

    }

    //this is used for creating the date object and get the date picker
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(eventDate);
        }
    };

    //updating the buttons with selected data from date picker
    private void updateLabel(Button mView) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mView.setText(sdf.format(myCalendar.getTime()));
    }

    /*
    * used to convert the string to date object so that
    * we can compare the objects for form date and to date
    * */
    private Date getDateFromString(String mDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return format.parse(mDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(MentorPostEventActivity.this)) {
            showNoNetworkDialogue(MentorPostEventActivity.this);
        }
    }
}
