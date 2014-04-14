package com.flyerfive.client.data;

import com.flyerfive.client.model.Fcontainer;

public class ContainerProperty<T extends Fcontainer> extends NodeProperty<T> {
	public ContainerProperty() {
		setType(PropertyType.Shape);
    }
	
	@Override
	public void fromFnode(T fnode) {
	    super.fromFnode(fnode);
	}
}
