package com.antrromet.wecare.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.antrromet.wecare.Application;
import com.antrromet.wecare.Constants;
import com.antrromet.wecare.R;
import com.antrromet.wecare.interfaces.OnVolleyResponseListener;
import com.antrromet.wecare.utils.Logger;

import org.json.JSONObject;


public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    private OnVolleyResponseListener mVolleyListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Show toast method from any child fragment
     *
     * @param id the resourceId for the string message to be displayed
     */
    protected void showToast(int id) {
        if (getActivity() != null) {
            String msg = getString(id);
            if (!TextUtils.isEmpty(msg)) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Show toast method from any child fragment
     *
     * @param msg the string message to be displayed
     */
    protected void showToast(String msg) {
        if (getActivity() != null && !TextUtils.isEmpty(msg)) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks whether internet is available or not
     *
     * @return true if internet is present else return false
     */
    protected boolean isNetworkAvailable() {
        if (getActivity() != null) {
            final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                return true;
            } else {
                showToast(R.string.internet_problem);
                return false;
            }
        }
        return false;
    }

    /**
     * Hide the keyboard from whichever view has the focus
     */
    protected void hideKeyboard() {
        if (getActivity() != null) {
            // Check if no view has focus:
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * Sets the listener for Volley callbacks
     *
     * @param listener to callback when the volley request is complete
     */
    protected void setVolleyListener(OnVolleyResponseListener listener) {
        mVolleyListener = listener;
    }

    /**
     * Passes on the request to Volley to make the network call
     *
     * @param tag             to identify each request
     * @param url             the url to call
     * @param alertMsgDisplay the msg to display to user while the call is in progress
     */
    protected void requestVolley(final Constants.VolleyTags tag, int method, String url, JSONObject requestObject,
                                 String alertMsgDisplay) {
        if (isNetworkAvailable()) {
            Logger.d(TAG, url);
            hideKeyboard();
            ProgressDialog pDialog = null;
            // Show the dialog only when the alert message is not null
            if (!TextUtils.isEmpty(alertMsgDisplay)) {
                pDialog = new ProgressDialog(getActivity());
                // Cancel the request if the dialog is cancelled
                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Application.getRequestQueue().cancelAll(tag);
                    }
                });
                pDialog.setMessage(alertMsgDisplay);
                pDialog.show();
            }
            final ProgressDialog finalDialog = pDialog;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, requestObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if (finalDialog != null) {
                                finalDialog.dismiss();
                            }
                            Logger.d(TAG, response.toString());
                            if (mVolleyListener != null) {
                                mVolleyListener.OnSuccess(tag, response);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.e(TAG, "Error: " + error.getMessage());
                    showToast(error.getMessage());
                    if (finalDialog != null) {
                        finalDialog.dismiss();
                    }
                    if (mVolleyListener != null) {
                        mVolleyListener.OnError(tag, error);
                    }
                }
            });
            // Adding request to request queue
            Application.addToRequestQueue(jsonObjectRequest, tag.tag);
        }
    }

}
