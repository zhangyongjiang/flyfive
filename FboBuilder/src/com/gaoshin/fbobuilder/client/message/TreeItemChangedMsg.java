package com.gaoshin.fbobuilder.client.message;

import com.gaoshin.fbobuilder.client.data.ResourceProperty;

public class TreeItemChangedMsg extends Message {
	private ResourceProperty item;

	public TreeItemChangedMsg(ResourceProperty rp) {
		this.setItem(rp);
    }

	public ResourceProperty getItem() {
	    return item;
    }

	public void setItem(ResourceProperty item) {
	    this.item = item;
    }
}
