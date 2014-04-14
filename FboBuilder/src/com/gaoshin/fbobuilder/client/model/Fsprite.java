package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Sprite;

import com.gaoshin.fbobuilder.client.data.SpriteProperty;

public class Fsprite extends Fshape<Sprite, SpriteProperty> {

	public Fsprite(Flayer flayer, SpriteProperty sp) {
	    super(flayer, sp);
    }

	@Override
    protected Sprite createKineticNode() {
	    return null;
    }

	@Override
    public SpriteProperty createResourceProperty() {
	    return null;
    }

}
