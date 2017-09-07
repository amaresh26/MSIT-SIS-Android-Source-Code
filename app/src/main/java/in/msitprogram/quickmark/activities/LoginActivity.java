package in.msitprogram.quickmark.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.mentor.MentorDashboardActivity;
import in.msitprogram.quickmark.activities.student.DashboardActivity;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.Permissions;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 18/05/17.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout inputLayoutUsername;
    private AppCompatEditText inputUsername;
    private TextInputLayout inputLayoutPassword;
    private AppCompatEditText inputPassword;
    private Button loginBtn;
    private TextView forgotPasswordTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Permissions permissions = new Permissions(LoginActivity.this);
            permissions.requestPermissionsAllPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE});
        }
        //getting the views initialized
        findViews();
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        inputLayoutUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputUsername = (AppCompatEditText) findViewById(R.id.input_username);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputPassword = (AppCompatEditText) findViewById(R.id.input_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        forgotPasswordTv = (TextView) findViewById(R.id.forgot_password_tv);

        loginBtn.setOnClickListener(this);
        forgotPasswordTv.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-18 17:07:45 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            loginVerification();
        } else if (v == forgotPasswordTv) {

        }
    }

    /*
    * verifying the username and password given by the user
    * if  user exists then return true if not show error
    * */
    private boolean loginVerification() {
        String username = getData(inputUsername);
        String password = getData(inputPassword);

        if (username.equals("")) {
            inputLayoutUsername.setError("Please enter the Username");
            inputUsername.requestFocus();
            return FALSE;
        } else {
            inputLayoutUsername.setError("");
        }
        if (password.equals("")) {
            inputLayoutPassword.setError("Please enter the Password");
            inputPassword.requestFocus();
            return FALSE;
        } else {
            inputLayoutPassword.setError("");
        }
        //this is to check the network connection if network is not avilable it will through the error
        if (!Networking.isNetworkAvailable(LoginActivity.this)) {
            showNoNetworkDialogue(LoginActivity.this);
            return FALSE;
        }

        /*
        * calling the login Api from this method
        * */
        loginApiCall(username, password);
        return TRUE;
    }

    /*
    * calling the web services for user Authentication
    * @param1 username
    * @param2 password
    * if authentication is successful userdata stored in the session manager for using it in
    * further api calls and profile display
    * */
    private void loginApiCall(String username, String password) {
        HashMap<String, String> mMap = new HashMap<>();
        mMap.put("username", username);
        mMap.put("password", password);
        mMap.put("gcm_id", mSessionManager.getFcmId());
        new VolleyTask(LoginActivity.this, Constants.LOGIN_URL, mMap, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                //handleResponse
                try {
                    JSONObject mMainObject = new JSONObject(response);
                    if (mMainObject.getString("result").equals("success")) {
                        JSONObject data = mMainObject.getJSONObject("data");
                        String guid = data.getString("guid");
                        JSONObject user_data = data.getJSONObject("user_data");
                        String uid = user_data.getString("uid");
                        String full_name = user_data.getString("full_name");
                        String user_type = user_data.getString("user_type");
                        String roll_number = user_data.getString("roll_number");
                        String email = user_data.getString("email");
                        String user_img = user_data.getString("user_img");
                        String mobile = user_data.getString("mobile");
                        String parent_name = user_data.getString("parent_name");
                        String parent_mobile = user_data.getString("parent_mobile");
                        String parent_email = user_data.getString("parent_email");

                        //saving the user data in sessions (Shared Preference)
                        mSessionManager.saveFullName(full_name);
                        mSessionManager.saveUserId(uid);
                        mSessionManager.saveGUID(guid);
                        mSessionManager.saveRollNumber(roll_number);
                        mSessionManager.saveEmail(email);
                        mSessionManager.saveMobileNo(mobile);
                        mSessionManager.saveParentName(parent_name);
                        mSessionManager.saveParentMobileNo(parent_mobile);
                        mSessionManager.saveParentEmail(parent_email);
                        mSessionManager.saveUserImg(user_img);
                        mSessionManager.saveUserType(user_type);

                        if (user_type.equals("0")) {
                            //welcome msg
                            makeToast("Welcome " + full_name, LONG);
                            // redirecting to the student dashboard if usertype = 0
                            nextIntent(DashboardActivity.class, null, TRUE);
                        } else if (user_type.equals("1")) {
                            //welcome msg
                            makeToast("Welcome " + full_name, LONG);
                            // redirecting to the Mentor dashboard if usertype = 1
                            nextIntent(MentorDashboardActivity.class, null, TRUE);

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
}
