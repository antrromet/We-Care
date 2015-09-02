package com.antrromet.wecare.interfaces;

import com.android.volley.VolleyError;
import com.antrromet.wecare.Constants;

public interface OnVolleyResponseListener {

	void OnSuccess(Constants.VolleyTags tag, Object responseObject);

	void OnError(Constants.VolleyTags tag, VolleyError error);
}
