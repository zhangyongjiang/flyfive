package com.gaoshin.fbobuilder.client.service;

import com.google.gwt.json.client.JSONObject;


public interface FileManagerCallback {
	void onSuccess(JSONObject jo);
	void onError(int code, String msg);
}
