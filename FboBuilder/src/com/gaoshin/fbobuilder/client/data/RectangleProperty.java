package com.gaoshin.fbobuilder.client.data;

import net.edzard.kinetic.Rectangle;

import com.gaoshin.fbobuilder.client.model.Frectangle;

public class RectangleProperty extends ShapeProperty<Frectangle> {
	public static final String NameWidth = "width";
	public static final String NameHeight = "height";
	public static final String NameCornerRadius = "corner radius";
	
	public RectangleProperty() {
		setType(PropertyType.Rectangle);
    }
	
	@Override
	public void fromFnode(Frectangle fshape) {
	    super.fromFnode(fshape);
		updateOrCreateDoubleProperty(NameWidth, fshape.getNode().getWidth());
		updateOrCreateDoubleProperty(NameHeight, fshape.getNode().getHeight());
		updateOrCreateDoubleProperty(NameCornerRadius, fshape.getNode().getCornerRadius());
	}
	
	@Override
	public void toFnode(Frectangle fnode) {
	    super.toFnode(fnode);
	    fnode.getNode().setWidth(getDoubleProperty(NameWidth));
	    fnode.getNode().setHeight(getDoubleProperty(NameHeight));
	    fnode.getNode().setCornerRadius(getDoubleProperty(NameCornerRadius));
	}
	
	public void updateResourceProperty(Frectangle fnode, String... keys) {
		super.updateResourceProperty(fnode, keys);
		for(String key : keys) {
			if(key.equals(NameWidth)) {
				updateOrCreateDoubleProperty(NameWidth, fnode.getNode().getWidth());
			}
			else if(key.equals(NameHeight)) {
				updateOrCreateDoubleProperty(NameHeight, fnode.getNode().getHeight());
			}
			else if(key.equals(NameCornerRadius)) {
				updateOrCreateDoubleProperty(NameCornerRadius, fnode.getNode().getCornerRadius());
			}
		}
	}
	
	@Override
	public boolean updateView(Frectangle fnode, PrimeProperty rp) {
	    boolean found = super.updateView(fnode, rp);
	    if(found) return true;
		
		Rectangle shape = (Rectangle)fnode.getNode();
		if(rp.getName().equals(RectangleProperty.NameWidth)) {
			shape.setWidth(((DoubleProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(RectangleProperty.NameHeight)) {
			shape.setHeight(((DoubleProperty)rp).getValue());
			found = true;
		}
		if(rp.getName().equals(RectangleProperty.NameCornerRadius)) {
			shape.setCornerRadius(((DoubleProperty)rp).getValue());
			found = true;
		}
	    return found;
	}
}
