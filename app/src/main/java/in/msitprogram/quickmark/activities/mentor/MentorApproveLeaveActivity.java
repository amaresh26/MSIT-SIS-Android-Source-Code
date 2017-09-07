package in.msitprogram.quickmark.activities.mentor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.models.LeaveStatusModel;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 06/06/17.
 */

public class MentorApproveLeaveActivity extends BaseActivity implements View.OnClickListener {

    private static final String APPROVE = "0";
    private static final String CANCEL = "2";
    private TextInputLayout inputLayoutLeaveReason;
    private AppCompatEditText inputLeaveReason;
    private Button fromDateBtn;
    private Button toDateBtn;
    private Button approveLeaveBtn;
    private Button cancelLeaveBtn;
    private LeaveStatusModel mLeaveStatusModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approve);
        setToolbar("Leave Approval");
        findViews();
        //getting the event details from the event list in intent
        getBundleData(savedInstanceState);
        //set the data which is from intent
        setLeaveData();
    }

    /*
    *setting the data from the model passed by the intent
    * */
    private void setLeaveData() {
        inputLeaveReason.setText(mLeaveStatusModel.getmResaon());
        fromDateBtn.setText(mLeaveStatusModel.getmFromDate());
        toDateBtn.setText(mLeaveStatusModel.getmToDate());
    }

    /**
     * Find the Views in the layout<br />
     */
    private void findViews() {
        inputLayoutLeaveReason = (TextInputLayout) findViewById(R.id.input_layout_leave_reason);
        inputLayoutLeaveReason.setEnabled(false);
        inputLeaveReason = (AppCompatEditText) findViewById(R.id.input_leave_reason);
        inputLeaveReason.setEnabled(false);
        fromDateBtn = (Button) findViewById(R.id.from_date_btn);
        toDateBtn = (Button) findViewById(R.id.to_date_btn);
        approveLeaveBtn = (Button) findViewById(R.id.approve_leave_btn);
        cancelLeaveBtn = (Button) findViewById(R.id.cancel_leave_btn);

        approveLeaveBtn.setOnClickListener(this);
        cancelLeaveBtn.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     */
    @Override
    public void onClick(View v) {
        if (v == approveLeaveBtn) {
            // approved is 0
            leaveApproveApiCall(APPROVE);
        } else if (v == cancelLeaveBtn) {
            // cancel is 2
            leaveApproveApiCall(CANCEL);
        }
    }

    /*
    * this is to send the approve status for the leave applied
    * */
    private void leaveApproveApiCall(String status) {
        HashMap<String, String> mParam = new HashMap();
        mParam.put("guid", mSessionManager.getGUID());
        mParam.put("approve_status", status);
        mParam.put("lid", mLeaveStatusModel.getLid());

        new VolleyTask(MentorApproveLeaveActivity.this, Constants.LEAVE_APPROVE_MENTOR, mParam, "Please Wait..") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObj = new JSONObject(response);
                    if (mMainObj.getString("result").equals("success")) {
                        finish();
                    }
                } catch (JSONException e) {
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
                mLeaveStatusModel = null;
            } else {
                mLeaveStatusModel = (LeaveStatusModel) extra.getSerializableExtra("Leave_approve");
            }
        } else {
            mLeaveStatusModel = (LeaveStatusModel) extra.getSerializableExtra("Leave_approve");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(MentorApproveLeaveActivity.this)){
            showNoNetworkDialogue(MentorApproveLeaveActivity.this);
        }
    }
}