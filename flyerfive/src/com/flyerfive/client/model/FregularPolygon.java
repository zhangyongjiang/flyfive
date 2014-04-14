package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.RegularPolygon;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.RegularPolygonProperty;
import com.flyerfive.client.model.Controller.ControllerAnchor;

public class FregularPolygon extends Fshape<RegularPolygon, RegularPolygonProperty> {

	public FregularPolygon(Flayer flayer, RegularPolygonProperty rpp, FlyerContext cxt) {
	    super(flayer, rpp, cxt);
    }

	@Override
    public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
		Vector2d center = getNode().getPosition();
		double radius = getNode().getRadius();
		Vector2d pos = new Vector2d();
		pos.x = radius;
		pos.y = 0;
		list.add(new MarkedPosition(node.getPosition(), 0, pos));
	    return list;
    }

	@Override
    protected RegularPolygon createKineticNode() {
		Vector2d size = getStage().getSize();
	    Vector2d scale = getStage().getScale();
	    Vector2d position = new Vector2d();
	    position.x = size.x / 2;
	    position.y = size.y / 2;
	    double radius = size.x / 4 / scale.x;
		return Kinetic.createRegularPolygon(position, radius, 5);
    }

	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		double offsetx = Math.abs(newPos.x);
		double offsety = Math.abs(newPos.y);
		node.setRadius(offsetx);
		getResourceProperty().updateRadius(offsetx);
		return false;
	}

	@Override
    public RegularPolygonProperty createResourceProperty() {
		RegularPolygonProperty rpp = new RegularPolygonProperty();
		rpp.fromFnode(this);
	    return rpp;
    }

	public void load(RegularPolygonProperty rp) {
		rp.toFnode(this);
		setNodeProperty(rp);
    }
}
