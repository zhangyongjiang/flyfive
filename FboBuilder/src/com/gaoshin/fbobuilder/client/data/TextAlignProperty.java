package com.gaoshin.fbobuilder.client.data;

import net.edzard.kinetic.Text.HorizontalAlignment;

public class TextAlignProperty extends PrimeProperty<HorizontalAlignment> {
	
	public void setStringValue(String value) {
		setValue(HorizontalAlignment.valueOf(value));
	}
}
