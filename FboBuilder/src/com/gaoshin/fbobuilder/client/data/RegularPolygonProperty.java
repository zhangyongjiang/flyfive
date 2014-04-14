package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.model.FregularPolygon;

public class RegularPolygonProperty extends ShapeProperty<FregularPolygon> {
	public static final String NameRadius = "radius";
	public static final String NameSides = "points";
	
	public RegularPolygonProperty() {
		setType(PropertyType.RegularPolygon);
    }
	
	@Override
	public void fromFnode(FregularPolygon fshape) {
	    super.fromFnode(fshape);
		updateOrCreateDoubleProperty(NameRadius, fshape.getNode().getRadius());
		updateOrCreateIntProperty(NameSides, fshape.getNode().getSides());
	}
	
	@Override
	public void toFnode(FregularPolygon fnode) {
	    super.toFnode(fnode);
	    fnode.getNode().setRadius(getDoubleProperty(NameRadius));
	    fnode.getNode().setSides(getIntProperty(NameSides));
	}
	
	@Override
	public boolean updateView(FregularPolygon fnode, PrimeProperty rp) {
	    if(super.updateView(fnode, rp)) 
	    	return true;
	    
	    if(rp.getName().equals(NameRadius)) {
	    	fnode.getNode().setRadius(((DoubleProperty)rp).getValue());
	    	return true;
	    }
	    
	    if(rp.getName().equals(NameSides)) {
	    	fnode.getNode().setSides(((IntProperty)rp).getValue());
	    	return true;
	    }
	    
	    return false;
	}

	public void updateRadius(double radius) {
		updateOrCreateDoubleProperty(NameRadius, radius);
    }
}
