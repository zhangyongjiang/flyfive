package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.model.Fstar;

public class StarProperty extends ShapeProperty<Fstar> {
	
	public StarProperty() {
		setType(PropertyType.Star);
    }
	
	@Override
	public void fromFnode(Fstar fshape) {
	    super.fromFnode(fshape);
	}
}
