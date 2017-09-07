package in.msitprogram.quickmark.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public abstract class VolleyTask {

    private Context context;
    private ProgressDialog mDialog;
    private String progressMessage;

    //constructor with parameters
    public VolleyTask(Context context, String url, Map<String, String> params, String progressMessage) {
        this.context = context;
        this.progressMessage = progressMessage;
        if (progressMessage != null && progressMessage.trim().length() > 1)
            showDialog(this.progressMessage);
        if (Networking.isNetworkAvailable(context))
            performTask(params, url);
        else
            Toast.makeText(context, "Internet is required", Toast.LENGTH_LONG).show();
    }

    private void performTask(final Map<String, String> params, final String url) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mRequest = new StringRequest(Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(url,response);
                hideDialog();
                handleResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                handleError(error);
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Log.e("Params", params.toString());
                return params;
            }
        };
        mRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequest.setShouldCache(true);
        mRequestQueue.add(mRequest);
    }
    //abstract methods for handling the error
    protected abstract void handleError(VolleyError error);
    //abstract method for handling the response
    protected abstract void handleResponse(String response);

    // to show the dialog while waiting for the response
    public void showDialog(String message) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(message);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    //dismissing the dialog after the response or error is received
    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

}