package com.flyerfive.client.data;

public class IntProperty extends PrimeProperty<Integer> {
	@Override
	public void setStringValue(String value) {
		if(value == null)
			setValue(null);
		else
			setValue(Integer.parseInt(value));
	}
}
