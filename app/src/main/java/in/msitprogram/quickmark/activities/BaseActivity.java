package in.msitprogram.quickmark.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.utils.SessionManager;


/**
 * Created by amareshjana on 18/05/17.
 * This Class is the super class of all activities which will have all the common function used
 * thought out the application.
 */

public class BaseActivity extends AppCompatActivity {

    //this is to show the tag in Log to find the errors
    private final static String TAG = "BASEACTIVITY_";
    //context is to get the context of every class
    public Context mContext;
    //to access the data which is stored in the session manager
    public static SessionManager mSessionManager;
    public static final int SHORT = 0;
    public static final int LONG = 1;
    public static final boolean TRUE = true;
    public static final boolean FALSE = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mSessionManager = new SessionManager(mContext);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //clicking on the back button
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*this method is to setup the tool bar for the activity
     * @parameter title - title of the activity
     */
    public Toolbar setToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolBarTitle.setText(title);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            toolBarTitle.setText(title);
        }
        return toolbar;
    }

    /*
     * this is to make the toast just sending the string and * int value as 0 =
	 * long & 1 = short length of the toast
	 */
    public void makeToast(String msg, int length) {
        if (length == LONG)
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /*to get the data from the string for the View
    * @param mView will only accepts EditText, TextView and Button
    * */
    public String getData(View mView) {
        String data = "";
        if (mView instanceof EditText) {
            EditText mEditText = (EditText) mView;
            data = mEditText.getText().toString().trim();
        } else if (mView instanceof TextView) {
            TextView mTextView = (TextView) mView;
            data = mTextView.getText().toString().trim();
        } else if (mView instanceof Button) {
            Button mButton = (Button) mView;
            data = mButton.getText().toString().trim();
        }
        return data;
    }

    /* to set the data to the View
    * @param mView will only accepts EditText, TextView and Button
    * @param msg will only accepts the String type which should be displayed in the view
    * */
    public void setData(View mView, String msg) {
        if (mView instanceof EditText) {
            EditText mEditText = (EditText) mView;
            mEditText.setText(msg);
        } else if (mView instanceof TextView) {
            TextView mTextView = (TextView) mView;
            mTextView.setText(msg);
        } else if (mView instanceof Button) {
            Button mButton = (Button) mView;
            mButton.setText(msg);
        }
    }

    /* to send from one class to another with extras or without extras or with finish or with our finish
    * @param nextClass is to which class we need to intent
    * @param values is a hashmap to send the
    * */
    public void nextIntent(Class<?> nextClass, HashMap<Object, Object> values, boolean withFinish) {
        Intent nextIntent = new Intent(mContext, nextClass);
        if (values == null) {
            startActivity(nextIntent);
            if (withFinish)
                finish();
        } else {
            String keys[] = Arrays.copyOf(values.keySet().toArray(), values.keySet().toArray().length, String[].class);
            for (String key : keys) {
                nextIntent.putExtra(key, (Serializable) values.get(key));
            }
            startActivity(nextIntent);
            if (withFinish)
                finish();
        }
    }

    /*
    * to show the dialogue if network is not available
    * @param mContext is to send the context of the method calling class
    * */
    public void showNoNetworkDialogue(Context mContext) {
        new AlertDialog.Builder(mContext)
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Turn On Internet",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /*
    * when automatic keypad is opening use this to close the keypad
    * */
    public void closeKeypad() {
        //to close the keypad because while opening the activity keypad will be opended
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}
