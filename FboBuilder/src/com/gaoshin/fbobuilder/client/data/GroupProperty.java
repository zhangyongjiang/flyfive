package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.model.Fgroup;

public class GroupProperty<T extends Fgroup> extends ContainerProperty<T> {
	public GroupProperty(T line) {
		setType(PropertyType.Line);
    }
	
	@Override
	public void fromFnode(T fnode) {
	    super.fromFnode(fnode);
	}
}
