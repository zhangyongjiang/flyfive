package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.model.Flayer;

public class LayerProperty extends ContainerProperty<Flayer> {
	
	public LayerProperty() {
		setType(PropertyType.Layer);
		setName("Layer");
    }
	
	@Override
	public void fromFnode(Flayer fnode) {
	    super.fromFnode(fnode);
	}
}
