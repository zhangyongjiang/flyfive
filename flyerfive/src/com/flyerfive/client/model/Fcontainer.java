package com.flyerfive.client.model;

import net.edzard.kinetic.Container;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.ContainerProperty;

public abstract class Fcontainer<T extends Container, P extends ContainerProperty> extends Fnode<T, P> {

	public Fcontainer(Flayer flayer, P cp, FlyerContext cxt) {
	    super(flayer, cp, cxt);
    }

}
