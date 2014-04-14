package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Line;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.LineProperty;
import com.gaoshin.fbobuilder.client.model.Controller.ControllerAnchor;

public class Fline extends Fshape<Line, LineProperty> {
	private static final Logger logger = Logger.getLogger("Fline");
	
	public Fline(Flayer flayer, LineProperty lp) {
	    super(flayer, lp);
    }

	@Override
    protected Line createKineticNode() {
		Vector2d size = getStage().getSize();
	    Vector2d scale = getStage().getScale();
	    Vector2d start = new Vector2d();
		Vector2d end = new Vector2d();
	    start.x = 0;
	    start.y = 0;
	    end.x = size.x / 2 / scale.x;
	    end.y = size.y / 2 / scale.y;
		Line line = Kinetic.createLine(start, end);
		line.setStroke(Colour.darkcyan);
		line.setStrokeWidth(6);
		line.setPosition(new Vector2d(size.x/4, size.y/4));
		return line;
    }

	@Override
    public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
		Vector2d end = getNode().getEnd();
		Vector2d start = node.getStart();
//		list.add(new MarkedPosition(node.getPosition(), "start", start));
		list.add(new MarkedPosition(node.getPosition(), "end", end));
	    return list;
    }
	
	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		if("start".equals(anchor.mp.getId())) {
			node.setStart(newPos);
		}
		else {
			node.setEnd(newPos);
			getResourceProperty().updateOrCreateDoubleProperty(LineProperty.NameEndPosX, node.getEnd().x);
			getResourceProperty().updateOrCreateDoubleProperty(LineProperty.NameEndPosY, node.getEnd().y);
		}
		
		return false;
	}
	
	public boolean isRotatable() {
		return false;
	}

	@Override
    public LineProperty createResourceProperty() {
	    LineProperty property = new LineProperty();
		property.fromFnode(this);
		return property;
    }

	public void load(LineProperty rp) {
		rp.toFnode(this);
		setNodeProperty(rp);
    }
}
