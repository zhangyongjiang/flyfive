package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Ellipse;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.EllipseProperty;
import com.gaoshin.fbobuilder.client.model.Controller.ControllerAnchor;

public class Fellipse extends Fshape<Ellipse, EllipseProperty> {

	public Fellipse(Flayer flayer, EllipseProperty ep) {
	    super(flayer, ep);
    }

	@Override
    public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
		Vector2d center = getNode().getPosition();
		Vector2d radius = getNode().getRadius();
		
		Vector2d vpos = new Vector2d();
		vpos.x = - radius.x;
		vpos.y = - radius.y;
		list.add(new MarkedPosition(node.getPosition(), "topleft", vpos));
		
		Vector2d hpos = new Vector2d();
		hpos.x = radius.x;
		hpos.y = radius.y;
		list.add(new MarkedPosition(node.getPosition(), "rightbottom", hpos));
		
	    return list;
    }

	@Override
    protected Ellipse createKineticNode() {
		Vector2d size = getStage().getSize();
	    Vector2d position = new Vector2d();
	    Vector2d scale = getStage().getScale();
	    position.x = size.x / 2;
	    position.y = size.y / 2;
		Vector2d radius = new Vector2d();
	    radius.x = position.x / 1.5 / scale.x;
	    radius.y = position.y / 1.5 / scale.y;
		return Kinetic.createEllipse(position, radius);
    }

	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		Vector2d radius = new Vector2d();
		radius.x = Math.abs(newPos.x);
		radius.y = Math.abs(newPos.y);
		node.setRadius(radius);
		getResourceProperty().updateRadius(radius);
		return true;
	}

	@Override
    public EllipseProperty createResourceProperty() {
		EllipseProperty prop = new EllipseProperty();
		prop.fromFnode(this);
	    return prop;
    }

	public void load(EllipseProperty rp) {
		rp.toFnode(this);
		setNodeProperty(rp);
    }
}
