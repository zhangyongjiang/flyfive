package com.gaoshin.fbobuilder.client.data;

import net.edzard.kinetic.Shape.LineJoin;

public class LineJoinProperty extends PrimeProperty<LineJoin> {
	
	public void setStringValue(String value) {
		setValue(LineJoin.valueOf(value));
	}
}
