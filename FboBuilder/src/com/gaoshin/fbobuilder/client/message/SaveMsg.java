package com.gaoshin.fbobuilder.client.message;

import com.gaoshin.fbobuilder.client.data.ResourceProperty;


public class SaveMsg extends Message {
	private ResourceProperty rp;
	
	public SaveMsg(ResourceProperty rp) {
		this.setRp(rp);
    }

	public ResourceProperty getRp() {
	    return rp;
    }

	public void setRp(ResourceProperty rp) {
	    this.rp = rp;
    }
}
