package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Star;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.StarProperty;

public class Fstar extends Fshape<Star, StarProperty> {

	public Fstar(Flayer flayer, StarProperty sp) {
	    super(flayer, sp);
    }

	@Override
    protected Star createKineticNode() {
		Vector2d position = getCenterPosition();
		Vector2d size = getStageSize();
		double innerR = size.x / 4;
		double outerR = size.x * 3 / 4;
		int num = 5;
		Star star = Kinetic.createStar(position, innerR, outerR, num );
	    return star;
    }

	@Override
    public StarProperty createResourceProperty() {
	    return null;
    }

}
