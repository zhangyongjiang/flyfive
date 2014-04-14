package com.flyerfive.client.model;

import net.edzard.kinetic.Group;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.GroupProperty;

public abstract class Fgroup<T extends Group, P extends GroupProperty> extends Fcontainer<T, P> {

	public Fgroup(Flayer flayer, P gp, FlyerContext cxt) {
	    super(flayer, gp, cxt);
    }

}
