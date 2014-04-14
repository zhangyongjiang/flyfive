package com.gaoshin.fbobuilder.client.data;

import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.model.Fline;

public class LineProperty extends ShapeProperty<Fline> {
	public static final String NameEndPosX = "end position x";
	public static final String NameEndPosY = "end position y";

	public LineProperty() {
		setType(PropertyType.Line);
    }
	
	@Override
	public void fromFnode(Fline fshape) {
	    super.fromFnode(fshape);
	    Vector2d end = fshape.getNode().getEnd();
		updateOrCreateDoubleProperty(NameEndPosX, end.x);
		updateOrCreateDoubleProperty(NameEndPosY, end.y);
	}
	
	@Override
	public boolean updateView(Fline fnode, PrimeProperty rp) {
	    boolean found = super.updateView(fnode, rp);
	    if(found) return true;
	    
	    if(rp.getName().equals(NameEndPosX)) {
	    	Vector2d end = fnode.getNode().getEnd();
	    	end.x = ((DoubleProperty)rp).getValue();
	    	fnode.getNode().setEnd(end);
	    	found = true;
	    }
	    else if(rp.getName().equals(NameEndPosY)) {
	    	Vector2d end = fnode.getNode().getEnd();
	    	end.y = ((DoubleProperty)rp).getValue();
	    	fnode.getNode().setEnd(end);
	    	found = true;
	    }
		return found;
	}

	public void updateResourceProperty(Fline fnode, String... keys) {
	    super.updateResourceProperty(fnode, keys);
		for(String key : keys) {
			if(key.equals(NameEndPosX)) {
				updateOrCreateDoubleProperty(NameEndPosX, fnode.getNode().getEnd().x);
			}
			else if(key.equals(NameEndPosY)) {
				updateOrCreateDoubleProperty(NameEndPosY, fnode.getNode().getEnd().y);
			}
		}
	}
	
	@Override
	public void toFnode(Fline fnode) {
	    super.toFnode(fnode);
	    Double x = getDoubleProperty(NameEndPosX);
	    Double y = getDoubleProperty(NameEndPosY);
	    fnode.getNode().setEnd(new Vector2d(x, y));
	}

}
