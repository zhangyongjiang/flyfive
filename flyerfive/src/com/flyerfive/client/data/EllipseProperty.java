package com.flyerfive.client.data;

import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.model.Fellipse;

public class EllipseProperty extends ShapeProperty<Fellipse> {
	public static final String NameRadiusX = "radius.x";
	public static final String NameRadiusY = "radius.y";
	
	public EllipseProperty() {
		setType(PropertyType.Ellipse);
    }
	
	@Override
	public void fromFnode(Fellipse fshape) {
	    super.fromFnode(fshape);
		DoubleProperty radiusProp = new DoubleProperty();
		addProperty(radiusProp);
		radiusProp.setName(NameRadiusX);
		radiusProp.setValue(fshape.getNode().getRadius().x);
		
		DoubleProperty radiusYProp = new DoubleProperty();
		addProperty(radiusYProp);
		radiusYProp.setName(NameRadiusY);
		radiusYProp.setValue(fshape.getNode().getRadius().y);
	}
	
	@Override
	public void toFnode(Fellipse fnode) {
	    super.toFnode(fnode);
	    fnode.getNode().setRadius(new Vector2d(getDoubleProperty(NameRadiusX), getDoubleProperty(NameRadiusY)));
	}

	public void updateRadius(Vector2d radius) {
		updateOrCreateDoubleProperty(NameRadiusX, radius.x);
		updateOrCreateDoubleProperty(NameRadiusY, radius.y);
    }
}
