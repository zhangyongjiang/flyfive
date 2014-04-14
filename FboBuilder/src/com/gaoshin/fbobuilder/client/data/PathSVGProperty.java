package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.model.FpathSVG;

public class PathSVGProperty extends ShapeProperty<FpathSVG> {
	public PathSVGProperty() {
		setType(PropertyType.PathSVG);
    }
	
	@Override
	public void fromFnode(FpathSVG fshape) {
	    super.fromFnode(fshape);
	}
}
