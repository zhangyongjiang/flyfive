package com.flyerfive.client.data;

import java.util.ArrayList;
import java.util.List;

public abstract class ListProperty<T> extends PrimeProperty<List<T>> {
	public ListProperty() {
		setValue(new ArrayList<T>());
    }
	
	@Override
	public String getStringValue() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<value.size(); i++) {
			sb.append(value.get(i).toString()).append(",");
		}
		return sb.substring(0, sb.length()-1);
	}
}
