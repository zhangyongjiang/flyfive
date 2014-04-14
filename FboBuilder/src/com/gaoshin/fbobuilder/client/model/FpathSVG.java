package com.gaoshin.fbobuilder.client.model;

import java.util.List;

import net.edzard.kinetic.PathSVG;

import com.gaoshin.fbobuilder.client.data.PathSVGProperty;

public class FpathSVG extends Fshape<PathSVG, PathSVGProperty> {

	public FpathSVG(Flayer flayer, PathSVGProperty psp) {
	    super(flayer, psp);
    }

	@Override
    public List<MarkedPosition> getAnchors() {
		throw new RuntimeException("unimplemented");
    }

	@Override
    protected PathSVG createKineticNode() {
	    return null;
    }

	@Override
    public PathSVGProperty createResourceProperty() {
	    return null;
    }

}
