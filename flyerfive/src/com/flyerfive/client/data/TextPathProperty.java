package com.flyerfive.client.data;

import com.flyerfive.client.model.Ftext;

public class TextPathProperty extends ShapeProperty<Ftext> {
	
	public TextPathProperty() {
		setType(PropertyType.Text);
    }
	
	@Override
	public void fromFnode(Ftext fshape) {
	    super.fromFnode(fshape);
	}
}
