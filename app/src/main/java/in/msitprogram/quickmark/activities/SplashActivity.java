package in.msitprogram.quickmark.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.mentor.MentorDashboardActivity;
import in.msitprogram.quickmark.activities.student.DashboardActivity;
import in.msitprogram.quickmark.utils.SessionManager;

/**
 * Created by amareshjana on 18/05/17.
 */

public class SplashActivity extends Activity {
    private CountDownTimer mCounter;
    private int SPLASH_TIME_OUT = 2000;
    private SessionManager mSessionManager;
    private GoogleCloudMessaging gcm;
    private String regId;
    private String PROJECT_NUMBER = "300177185312";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSessionManager = new SessionManager(getApplicationContext());
        //this is to get the guid id of the applicaiton for testing
        Log.e("Guid", mSessionManager.getGUID());
        //counter will be initialized and started here
        startTimer();
        if (mSessionManager.getFcmId().equals("")) {
            //gcmid
            getRegId();
        }
    }

    //here we are making this class to wait for SPLASH_TIME_OUT given globally
    private void startTimer() {
        mCounter = new CountDownTimer(SPLASH_TIME_OUT, 1000) {
            @Override
            public void onFinish() {
                if (mSessionManager.getGUID().equalsIgnoreCase("")) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else if (mSessionManager.getUserType().equals("0")) {
                    Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                } else if (mSessionManager.getUserType().equals("1")) {
                    Intent i = new Intent(SplashActivity.this, MentorDashboardActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }
        };
        mCounter.start();
    }

    public void getRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regId;
                    Log.e("FCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    if (regId.length() > 3)
                        mSessionManager.saveFcmId(regId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute(null, null, null);
    }
}
