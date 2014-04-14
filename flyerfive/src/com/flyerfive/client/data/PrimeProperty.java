package com.flyerfive.client.data;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public abstract class PrimeProperty<T> extends ResourceProperty {
	abstract public void setStringValue(String value);
	
	protected T value;
	
	public PrimeProperty() {
		setType(PropertyType.Prime);
    }
	
	public PrimeProperty(String name, T value) {
		setType(PropertyType.Prime);
		setName(name);
		setValue(value);
    }
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
	    this.value = value;
    }
	
	public String getStringValue() {
		return value == null ? "" : value.toString();
	}

	public JSONObject toJsonObject(JSONObject subclass) {
		JSONObject obj = null;
		if(subclass != null)
			obj = subclass;
		else
			obj = new JSONObject();
		super.toJsonObject(obj);
		String v = getStringValue();
		if(v != null) {
			JSONValue jsonValue = new JSONString(v);
			obj.put("value", jsonValue);
		}
		return obj;
	}
	
	public void fromJsonObject(JSONObject jobj) throws Exception {
		super.fromJsonObject(jobj);
		JSONValue v = jobj.get("value");
		if(v != null)
			setStringValue(unquote(v.toString()));
	}
}
