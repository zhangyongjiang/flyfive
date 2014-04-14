package com.flyerfive.client.data;

import com.flyerfive.client.model.Fstar;

public class StarProperty extends ShapeProperty<Fstar> {
	
	public StarProperty() {
		setType(PropertyType.Star);
    }
	
	@Override
	public void fromFnode(Fstar fshape) {
	    super.fromFnode(fshape);
	}
}
