package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.model.Fcircle;

public class CircleProperty extends ShapeProperty<Fcircle> {
	public static final String NameRadius = "radius";
	
	public CircleProperty() {
		setType(PropertyType.Circle);
    }
	
	public void fromFnode(Fcircle circle) {
		super.fromFnode(circle);
		updateOrCreateDoubleProperty(NameRadius, circle.getNode().getRadius());
	}
	
	@Override
	public void toFnode(Fcircle fnode) {
	    super.toFnode(fnode);
	    fnode.getNode().setRadius(getDoubleProperty(NameRadius));
	}

	public void updateRadius(double radius) {
		updateOrCreateDoubleProperty(NameRadius, radius);
    }
	
	@Override
	public boolean updateView(Fcircle fnode, PrimeProperty rp) {
	    if(super.updateView(fnode, rp)) 
	    	return true;
	    
	    if(rp.getName().equals(NameRadius)) {
	    	fnode.getNode().setRadius(((DoubleProperty)rp).getValue());
	    	return true;
	    }
	    
	    return false;
	}
}
