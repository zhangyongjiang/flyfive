package com.flyerfive.client.model;

import java.util.List;

import net.edzard.kinetic.PathSVG;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.PathSVGProperty;

public class FpathSVG extends Fshape<PathSVG, PathSVGProperty> {

	public FpathSVG(Flayer flayer, PathSVGProperty psp, FlyerContext cxt) {
	    super(flayer, psp, cxt);
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
