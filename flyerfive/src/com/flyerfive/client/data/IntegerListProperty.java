package com.flyerfive.client.data;

public class IntegerListProperty extends ListProperty<Integer> {

	@Override
    public void setStringValue(String strval) {
	    value.clear();
	    for(String s : strval.split(",")) {
	    	value.add(Integer.parseInt(s));
	    }
    }

}
