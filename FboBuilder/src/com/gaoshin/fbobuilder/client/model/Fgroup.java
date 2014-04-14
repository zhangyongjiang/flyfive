package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Group;

import com.gaoshin.fbobuilder.client.data.GroupProperty;

public abstract class Fgroup<T extends Group, P extends GroupProperty> extends Fcontainer<T, P> {

	public Fgroup(Flayer flayer, P gp) {
	    super(flayer, gp);
    }

}
