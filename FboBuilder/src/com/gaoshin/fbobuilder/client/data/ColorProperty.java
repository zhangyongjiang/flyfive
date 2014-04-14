package com.gaoshin.fbobuilder.client.data;

import net.edzard.kinetic.Colour;

public class ColorProperty extends PrimeProperty<Colour> {

	@Override
	public void setStringValue(String webColour) {
		setValue(new Colour(webColour));
	}

}
