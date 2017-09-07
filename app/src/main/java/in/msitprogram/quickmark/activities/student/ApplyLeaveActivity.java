package in.msitprogram.quickmark.activities.student;

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
 * Created by amareshjana on 22/05/17.
 */

public class ApplyLeaveActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout inputLayoutLeaveReason;
    private AppCompatEditText inputLeaveReason;
    private Button fromDateBtn;
    private Button toDateBtn;
    private Button applyLeaveBtn;
    private Calendar myCalendar = Calendar.getInstance();
    private int CLICKED = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        //setting the tool bar
        setToolbar("Apply Leave");
        //finding the views
        findViews();
    }

    /**
     * Find the Views in the layout<br />
     */
    private void findViews() {
        inputLayoutLeaveReason = (TextInputLayout) findViewById(R.id.input_layout_leave_reason);
        inputLeaveReason = (AppCompatEditText) findViewById(R.id.input_leave_reason);
        fromDateBtn = (Button) findViewById(R.id.from_date_btn);
        toDateBtn = (Button) findViewById(R.id.to_date_btn);
        applyLeaveBtn = (Button) findViewById(R.id.apply_leave_btn);

        fromDateBtn.setOnClickListener(this);
        toDateBtn.setOnClickListener(this);
        applyLeaveBtn.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     */
    @Override
    public void onClick(View v) {
        if (v == fromDateBtn) {
            //setting the date form the datepicker
            new DatePickerDialog(ApplyLeaveActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            //updating the date from date picker
            CLICKED = 0;
        } else if (v == toDateBtn) {
            //setting the date from the date picker
            new DatePickerDialog(ApplyLeaveActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            //updating the date from date picker
            CLICKED = 1;
        } else if (v == applyLeaveBtn) {
            // Handle clicks for applyLeaveBtn
            validateApplyLeaveData();
        }
    }

    /*
    * validating the data for apply leave for applying the leave
    * */
    private void validateApplyLeaveData() {
        String mReason = getData(inputLeaveReason);
        try {
            Date mFromDate = getDateFromString(getData(fromDateBtn));
            Date mtoDate = getDateFromString(getData(toDateBtn));
            if (mReason.equals("")) {
                inputLayoutLeaveReason.setError("Enter Reason");
                inputLeaveReason.requestFocus();
                return;
            } else if (getData(fromDateBtn).equals("From Date")) {
                makeToast("Select the From Date", LONG);
            } else if (getData(toDateBtn).equals("To Date")) {
                makeToast("Select the To Date", LONG);
            } else if (mFromDate.compareTo(mtoDate) > 0) {
                makeToast("Please check the dates selected", LONG);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            makeToast("Select the from and to date", LONG);
        }
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put("guid", mSessionManager.getGUID());
        mParams.put("reason", mReason);
        mParams.put("from_date", getData(fromDateBtn));
        mParams.put("to_date", getData(toDateBtn));

        new VolleyTask(ApplyLeaveActivity.this, Constants.APPLY_LEAVE_URL, mParams, "Please wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        makeToast(mMainObj.getString("msg"), LONG);
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
            if (CLICKED == 0)
                updateLabel(fromDateBtn);
            else
                updateLabel(toDateBtn);
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
        if (!Networking.isNetworkAvailable(ApplyLeaveActivity.this)) {
            showNoNetworkDialogue(ApplyLeaveActivity.this);
        }
    }
}