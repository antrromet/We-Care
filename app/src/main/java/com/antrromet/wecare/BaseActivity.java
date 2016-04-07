package com.antrromet.wecare;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.antrromet.wecare.interfaces.OnVolleyResponseListener;
import com.antrromet.wecare.utils.Logger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Base class to display action bar
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    private OnVolleyResponseListener mVolleyListener;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application application = (Application) getApplication();
        mTracker = application.getDefaultTracker();
    }

    /**
     * Checks whether internet is available or not
     *
     * @return true if internet is present else return false
     */
    protected boolean isNetworkAvailable() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            showToast(getString(R.string.internet_problem));
            return false;
        }
    }

    protected void setVolleyListener(@NonNull OnVolleyResponseListener listener) {
        mVolleyListener = listener;
    }

    public void requestVolley(final Constants.VolleyTags tag, String url, int method,
                              JSONObject requestObject, String alertMsgDisplay, boolean
                                      showProgressDialog) {
        Logger.d(TAG, url);
        hideKeyboard();

        if (isNetworkAvailable() && !Application.containsRequest(tag)) {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage(alertMsgDisplay);
            pDialog.setCancelable(false);
            if (showProgressDialog && !TextUtils.isEmpty(alertMsgDisplay)) {
                pDialog.show();
            }
            if (!showProgressDialog) {
                showActionBarProgress();
            }

            JsonObjectRequest jsonObjRequest = new JsonObjectRequest(method, url,
                    requestObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Logger.d(TAG, response.toString());
                            Application.removeFromQueue(tag);
                            hideActionBarProgress();
                            pDialog.dismiss();
                            mVolleyListener.OnSuccess(tag, response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Application.removeFromQueue(tag);
                    hideActionBarProgress();
                    Logger.e(TAG, "Error: " + error.getMessage());
                    if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                        showToast(getString(R.string.internet_problem));
                    } else {
                        showToast(error.getMessage());
                    }

                    pDialog.dismiss();
                    mVolleyListener.OnError(tag, error);
                }
            });
            // Adding request to request queue
            Application.addToRequestQueue(jsonObjRequest, tag);
        }
    }

    public void requestVolley(final Constants.VolleyTags tag, String url,
                              String alertMsgDisplay, boolean showActionBarProgress) {
        Logger.d(TAG, url);
        hideKeyboard();

        if (isNetworkAvailable() && !Application.containsRequest(tag)) {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            if (!showActionBarProgress && !TextUtils.isEmpty(alertMsgDisplay)) {
                pDialog.show();
            }
            pDialog.setMessage(alertMsgDisplay);
            pDialog.show();

            JsonArrayRequest jsonObjRequest = new JsonArrayRequest(url,
                    new Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Application.removeFromQueue(tag);
                            pDialog.dismiss();
                            Logger.d(TAG, response.toString());
                            mVolleyListener.OnSuccess(tag, response);
                        }
                    }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Application.removeFromQueue(tag);
                    Logger.e(TAG, "Error: " + error.getMessage());
                    if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                        showToast(getString(R.string.internet_problem));
                    } else {
                        showToast(error.getMessage());
                    }
                    pDialog.dismiss();
                    mVolleyListener.OnError(tag, error);
                }
            });
            // Wait for 20s because this can be a long request
            jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.RETRY_TIME,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            Application.addToRequestQueue(jsonObjRequest, tag);
        }
    }

    /**
     * Hide the keyboard from whichever view has the focus
     */
    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showActionBarProgress() {
        if (findViewById(R.id.actionbar_layout) != null && findViewById(R.id.actionbar_layout)
                .findViewById(R.id.toolbar_progress_bar) != null) {
            findViewById(R.id.actionbar_layout).findViewById(R.id.toolbar_progress_bar)
                    .setVisibility(View.VISIBLE);
        }
    }

    private void hideActionBarProgress() {
        if (Application.isQueueEmpty()) {
            if (findViewById(R.id.actionbar_layout) != null && findViewById(R.id
                    .actionbar_layout).findViewById(R.id.toolbar_progress_bar) != null) {
                findViewById(R.id.actionbar_layout).findViewById(R.id.toolbar_progress_bar)
                        .setVisibility(View.GONE);
            }
        }
    }

    protected void setScreenName(String name) {
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
