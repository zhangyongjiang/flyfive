package com.flyerfive.client.data;

import com.flyerfive.client.model.Fgroup;

public class GroupProperty<T extends Fgroup> extends ContainerProperty<T> {
	public GroupProperty(T line) {
		setType(PropertyType.Line);
    }
	
	@Override
	public void fromFnode(T fnode) {
	    super.fromFnode(fnode);
	}
}
