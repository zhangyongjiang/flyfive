package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.model.Ftext;

public class TextPathProperty extends ShapeProperty<Ftext> {
	
	public TextPathProperty() {
		setType(PropertyType.Text);
    }
	
	@Override
	public void fromFnode(Ftext fshape) {
	    super.fromFnode(fshape);
	}
}
