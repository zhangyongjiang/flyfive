package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.TextPath;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.TextPathProperty;

public class FtextPath extends Fshape<TextPath, TextPathProperty> {

	public FtextPath(Flayer flayer, TextPathProperty tpp) {
	    super(flayer, tpp);
    }

	@Override
    protected TextPath createKineticNode() {
	    Vector2d position = getCenterPosition();
		String aText = "html5flyer.com";
		String d = "hello world";
		return Kinetic.createTextPath(position, aText, d);
    }

	@Override
    public TextPathProperty createResourceProperty() {
	    return null;
    }
}
