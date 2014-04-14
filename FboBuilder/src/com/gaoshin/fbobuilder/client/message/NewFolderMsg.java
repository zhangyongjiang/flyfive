package com.gaoshin.fbobuilder.client.message;

import com.gaoshin.fbobuilder.client.data.FlyerFolderProperty;

public class NewFolderMsg extends Message {
	public static interface Callback {
		void onCreate(FlyerFolderProperty ffp);
	}
	
	private String name;
	private Callback callback;

	public String getName() {
	    return name;
    }

	public void setName(String name) {
	    this.name = name;
    }

	public Callback getCallback() {
	    return callback;
    }

	public void setCallback(Callback callback) {
	    this.callback = callback;
    }
}
