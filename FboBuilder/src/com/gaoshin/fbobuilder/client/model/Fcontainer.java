package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Container;

import com.gaoshin.fbobuilder.client.data.ContainerProperty;

public abstract class Fcontainer<T extends Container, P extends ContainerProperty> extends Fnode<T, P> {

	public Fcontainer(Flayer flayer, P cp) {
	    super(flayer, cp);
    }

}
