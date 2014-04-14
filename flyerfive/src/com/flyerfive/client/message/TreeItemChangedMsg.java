package com.flyerfive.client.message;

import com.flyerfive.client.data.ResourceProperty;

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
