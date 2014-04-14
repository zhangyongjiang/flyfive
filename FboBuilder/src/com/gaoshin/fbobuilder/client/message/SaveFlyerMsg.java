package com.gaoshin.fbobuilder.client.message;

import com.gaoshin.fbobuilder.client.data.ResourceProperty;

public class SaveFlyerMsg extends Message {
	private ResourceProperty resourceProperty;

	public SaveFlyerMsg() {
    }
	
	public SaveFlyerMsg(ResourceProperty rp) {
		this.setResourceProperty(rp);
    }

	public ResourceProperty getResourceProperty() {
	    return resourceProperty;
    }

	public void setResourceProperty(ResourceProperty resourceProperty) {
	    this.resourceProperty = resourceProperty;
    }
}
