package com.gaoshin.fbobuilder.client.data;

public class IntegerListProperty extends ListProperty<Integer> {

	@Override
    public void setStringValue(String strval) {
	    value.clear();
	    for(String s : strval.split(",")) {
	    	value.add(Integer.parseInt(s));
	    }
    }

}
