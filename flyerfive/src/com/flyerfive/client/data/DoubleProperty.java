package com.flyerfive.client.data;

public class DoubleProperty extends PrimeProperty<Double> {
	@Override
	public void setStringValue(String value) {
		if(value == null)
			setValue(null);
		else
			setValue(Double.parseDouble(value));
	}
	
	@Override
	public String getStringValue() {
		if (value == null) return "";
		String v = "" + value;
		int pos = v.indexOf(".");
		if((pos + 4) < v.length())
			v = v.substring(0,  pos + 4);
		return v;
	}
}
