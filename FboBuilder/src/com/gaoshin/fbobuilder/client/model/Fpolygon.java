package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Polygon;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.PolygonProperty;
import com.gaoshin.fbobuilder.client.model.Controller.ControllerAnchor;

public class Fpolygon extends Fshape<Polygon, PolygonProperty> {

	public Fpolygon(Flayer flayer, PolygonProperty pp) {
	    super(flayer, pp);
    }

	@Override
    public List<MarkedPosition> getAnchors() {
		Vector2d pos = node.getPosition();
		List<MarkedPosition> anchors = new ArrayList<MarkedPosition>();
		List<Vector2d> points = node.getPoints();
		for(int i=0; i<points.size(); i++) {
			Vector2d v = points.get(i);
			anchors.add(new MarkedPosition(node.getPosition(), i, v));
		}
		return anchors;
    }

	@Override
    protected Polygon createKineticNode() {
		int sides = initNodeProperty.getIntProperty(PolygonProperty.NameSides, 3);
		Vector2d topleft = nearTopLeft();
		Vector2d bottomright = nearBottomRight();
		List<Vector2d> points = new ArrayList<Vector2d>();
		double stepx = (bottomright.x - topleft.x) / sides;
		double stepy = (bottomright.y - topleft.y) / sides;
		for(int i=0; i<sides-1; i++) {
			double x = stepx * i;
			double y = 0;
			points.add(new Vector2d(x, y));
		}
		Vector2d bottomLeft = nearBottomLeft();
		points.add(new Vector2d(0, bottomLeft.y - topleft.y));
		Polygon p = Kinetic.createPolygon(points.get(0), points.get(1), points.get(2));
		p.setPoints(points);
		p.setPosition(topleft);
		return p;
    }

	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		MarkedPosition pos = anchor.mp;
		Vector2d nodpos = node.getPosition();
		int index = (Integer) pos.getId();
		Vector2d v = new Vector2d();
		v.x = newPos.x;
		v.y = newPos.y;
		node.setPoint(index, v);
		getResourceProperty().updateOrCreateStrProperty(PolygonProperty.NamePoints, PolygonProperty.getPointsFromNode(this));
		return false;
	}

	@Override
    public PolygonProperty createResourceProperty() {
		PolygonProperty property = new PolygonProperty();
		property.fromFnode(this);
		return property;
    }
	
	public void load(PolygonProperty rp) {
		rp.toFnode(this);
		setNodeProperty(rp);
    }
}
