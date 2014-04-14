package com.flyerfive.client.model;

import net.edzard.kinetic.Sprite;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.SpriteProperty;

public class Fsprite extends Fshape<Sprite, SpriteProperty> {

	public Fsprite(Flayer flayer, SpriteProperty sp, FlyerContext cxt) {
	    super(flayer, sp, cxt);
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
