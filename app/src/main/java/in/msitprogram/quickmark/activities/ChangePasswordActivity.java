package in.msitprogram.quickmark.activities;

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
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 23/05/17.
 */

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout inputLayoutOldPassword;
    private AppCompatEditText inputOldPassword;
    private TextInputLayout inputLayoutNewPassword;
    private AppCompatEditText inputNewPassword;
    private TextInputLayout inputLayoutConformNewPassword;
    private AppCompatEditText inputConformNewPassword;
    private Button changePassword;

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        inputLayoutOldPassword = (TextInputLayout) findViewById(R.id.input_layout_old_password);
        inputOldPassword = (AppCompatEditText) findViewById(R.id.input_old_password);
        inputLayoutNewPassword = (TextInputLayout) findViewById(R.id.input_layout_new_password);
        inputNewPassword = (AppCompatEditText) findViewById(R.id.input_new_password);
        inputLayoutConformNewPassword = (TextInputLayout) findViewById(R.id.input_layout_conform_new_password);
        inputConformNewPassword = (AppCompatEditText) findViewById(R.id.input_conform_new_password);
        changePassword = (Button) findViewById(R.id.change_password);

        changePassword.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //toolbar name
        setToolbar("Change Password");
        //finding the views
        findViews();
    }

    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        if (v == changePassword) {
            // Handle clicks for changePassword
            //validation of inputs weather
            validateInputs();
        }
    }

    /*
    * checking the input old password and conform new password
    * */
    private void validateInputs() {
        String oldPassword = getData(inputOldPassword);
        String newPassword = getData(inputNewPassword);
        String conformNewPassword = getData(inputConformNewPassword);
        if (oldPassword.equals("")) {
            makeToast("Old Password required and missing", LONG);
            inputOldPassword.requestFocus();
            return;
        } else if (newPassword.equals("")) {
            makeToast("New Password required and missing", LONG);
            inputNewPassword.requestFocus();
            return;
        } else if (conformNewPassword.equals("")) {
            makeToast("Conform New Password required and missing", LONG);
            inputConformNewPassword.requestFocus();
            return;
        } else if (!newPassword.equals(conformNewPassword)) {
            makeToast("Password and conform password mismatch", LONG);
            inputNewPassword.setText("");
            inputConformNewPassword.setText("");
            return;
        } else if (newPassword.equals(oldPassword)) {
            makeToast("Old and New Password are same.", LONG);
            inputNewPassword.setText("");
            inputConformNewPassword.setText("");
            return;
        }
        HashMap<String, String> mParam = new HashMap<>();
        mParam.put("guid", mSessionManager.getGUID());
        mParam.put("password", newPassword);
        mParam.put("old_password", oldPassword);
        new VolleyTask(ChangePasswordActivity.this, Constants.CHANGE_PASSWORD, mParam, "Please wait...") {
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
                    } else if (mMainObj.getString("result").equals("failure")) {
                        makeToast(mMainObj.getString("msg"), LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(ChangePasswordActivity.this)) {
            showNoNetworkDialogue(ChangePasswordActivity.this);
        }
    }
}
