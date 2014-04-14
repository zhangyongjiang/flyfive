package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Circle;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.CircleProperty;
import com.flyerfive.client.model.Controller.ControllerAnchor;

public class Fcircle extends Fshape<Circle, CircleProperty> {
	
	public Fcircle(Flayer flayer, CircleProperty cp, FlyerContext cxt) {
	    super(flayer, cp, cxt);
    }

	@Override
	public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
		double radius = getNode().getRadius();
		Vector2d dragPos = new Vector2d();
		dragPos.x = radius;
		dragPos.y = 0;
		list.add(new MarkedPosition(node.getPosition(), 0, dragPos));
	    return list;
    }

	@Override
    protected Circle createKineticNode() {
		Vector2d size = getStage().getSize();
	    Vector2d position = new Vector2d();
	    Vector2d scale = getStage().getScale();
	    position.x = size.x / 2;
	    position.y = size.y / 2;
	    double r = Math.min(position.x, position.y) / 1.5 / scale.x;
		return Kinetic.createCircle(position, r);
    }

	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		double x = Math.abs(newPos.x);
		double y = Math.abs(newPos.y);
		double radius = Math.sqrt (x*x + y * y);
		node.setRadius(radius );
		getResourceProperty().updateRadius(radius);
		return false;
	}
	
	public boolean isRotatable() {
		return false;
	}

	@Override
	public CircleProperty createResourceProperty() {
		CircleProperty prop = new CircleProperty();
		prop.fromFnode(this);
	    return prop;
    }

	public void load(CircleProperty rp) {
		rp.toFnode(this);
		setNodeProperty(rp);
    }
}
