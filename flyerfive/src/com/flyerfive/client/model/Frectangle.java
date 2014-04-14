package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Rectangle;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.RectangleProperty;
import com.flyerfive.client.model.Controller.ControllerAnchor;

public class Frectangle extends Fshape<Rectangle, RectangleProperty> {
	private static final Logger logger = Logger.getLogger("Frectangle");

	public Frectangle(Flayer flayer, RectangleProperty rp, FlyerContext cxt) {
	    super(flayer, rp, cxt);
    }

	@Override
    public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
	    Vector2d topleft = new Vector2d(0, 0);
	    Vector2d rightbottom = new Vector2d(node.getWidth(), node.getHeight());
//	    list.add(new MarkedPosition(node.getPosition(), "topleft", topleft));
	    list.add(new MarkedPosition(node.getPosition(), "rightbottom", rightbottom));
	    return list;
    }

	@Override
    protected Rectangle createKineticNode() {
		Vector2d size = getStage().getSize();
	    Vector2d scale = getStage().getScale();
		double left = size.x / 4;
		double top = size.y / 4;
		Box2d box = new Box2d(0, 0, size.x / 2, size.y / 2);
		Rectangle rectangle = Kinetic.createRectangle(box);
		rectangle.setPosition(new Vector2d(left, top));
		return rectangle;
    }
	
	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		MarkedPosition pos = anchor.mp;
		if("topleft".equals(pos.getId())) {
			double offsetx = - newPos.x;
			double offsety = - newPos.y;
			node.setWidth(node.getWidth() + offsetx);
			node.setHeight(node.getHeight() + offsety);
			
			Vector2d v = Controller.convert(newPos, -node.getRotation());
			v.add(anchor.mp.getOriginalNodePos());
			node.setPosition(v);
			return true;
		}
		else if("rightbottom".equals(pos.getId())) {
			double offsetx = newPos.x - pos.getPos().x;
			double offsety = newPos.y - pos.getPos().y;
			node.setWidth(node.getWidth() + offsetx);
			node.setHeight(node.getHeight() + offsety);
			pos.getPos().x = newPos.x;
			pos.getPos().y = newPos.y;
			
			getResourceProperty().updateOrCreateDoubleProperty(RectangleProperty.NameWidth, node.getWidth());
			getResourceProperty().updateOrCreateDoubleProperty(RectangleProperty.NameHeight, node.getHeight());
		}
		return false;
	}

	@Override
    public RectangleProperty createResourceProperty() {
	    RectangleProperty property = new RectangleProperty();
		property.fromFnode(this);
		return property;
    }

	public void load(RectangleProperty rp) {
		rp.toFnode(this);
		setNodeProperty(rp);
    }
	
	@Override
	protected void setDefaultValue() {
	    super.setDefaultValue();
        Object defaultValue = ctx.getDefaultValue(RectangleProperty.NameCornerRadius);
        if(defaultValue != null) {
            node.setCornerRadius((Double) defaultValue);
        }
        else {
            node.setCornerRadius(getStageSize().x / 100);
        }
	}
}
