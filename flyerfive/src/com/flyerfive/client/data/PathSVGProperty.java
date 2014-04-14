package com.flyerfive.client.data;

import com.flyerfive.client.model.FpathSVG;

public class PathSVGProperty extends ShapeProperty<FpathSVG> {
	public PathSVGProperty() {
		setType(PropertyType.PathSVG);
    }
	
	@Override
	public void fromFnode(FpathSVG fshape) {
	    super.fromFnode(fshape);
	}
}
