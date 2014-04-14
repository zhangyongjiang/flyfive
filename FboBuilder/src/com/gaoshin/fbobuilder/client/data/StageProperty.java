package com.gaoshin.fbobuilder.client.data;

public class StageProperty extends ResourceProperty {
	private int index;
	
	public StageProperty() {
		setType(PropertyType.Layer);
    }
	
	public String getName() {
		return getIndex() + " - " + super.getName();
	}

	public int getIndex() {
	    return index;
    }

	public void setIndex(int index) {
	    this.index = index;
    }
}
