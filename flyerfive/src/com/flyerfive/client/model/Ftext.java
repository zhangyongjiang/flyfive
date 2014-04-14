package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Text;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.ExternalFunctions;
import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.NodeProperty;
import com.flyerfive.client.data.TextProperty;
import com.flyerfive.client.model.Controller.ControllerAnchor;
import com.google.gwt.user.client.Timer;

public class Ftext extends Fshape<Text, TextProperty> {
	public Ftext(Flayer flayer, TextProperty tp, FlyerContext cxt) {
	    super(flayer, tp, cxt);
    }

	@Override
    protected Text createKineticNode() {
	    Vector2d position = nearTopLeft();
		String aText = "Hello world!";
		if(initNodeProperty != null) {
			aText = initNodeProperty.getStrProperty(TextProperty.NameText);
		}
		Text text = Kinetic.createText(position, aText );
		text.setFontSize((int) (getStageSize().x / 20));
		text.setWidth(getStageSize().x * 3 / 5);
		return text;
    }
	
	@Override
    public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
	    Vector2d topleft = new Vector2d(0, 0);
//	    list.add(new MarkedPosition(node.getPosition(), "topleft", topleft));

	    Vector2d rightbottom = new Vector2d(node.getBoxWidth(), 0);
	    rightbottom.add(topleft);
	    list.add(new MarkedPosition(node.getPosition(), "rightbottom", rightbottom));
	    
	    return list;
    }

	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		MarkedPosition pos = anchor.mp;
		if(anchor.mp.getId().equals("topleft")) {
			Vector2d v = Controller.convert(newPos, -node.getRotation());
			v.add(anchor.mp.getOriginalNodePos());
			node.setPosition(v);
			updateResourceProperty(NodeProperty.NamePositionX, NodeProperty.NamePositionY);
		}
		else if(anchor.mp.getId().equals("rightbottom")) {
			double offsetx = newPos.x - pos.getPos().x;
			double offsety = newPos.y - pos.getPos().y;
			node.setWidth(node.getWidth() + offsetx);
			pos.getPos().x = newPos.x;
			
			getResourceProperty().updateOrCreateDoubleProperty(TextProperty.NameWidth, node.getWidth());
			
//			Vector2d v = Controller.convert(newPos, -node.getRotation());
//			v.add(anchor.mp.getOriginalNodePos());
//			node.setPosition(v);
//
//			double offsetx = newPos.x - pos.getPos().x;
//			double offsety = newPos.y - pos.getPos().y;
//			node.setWidth(node.getWidth() + offsetx);
//			node.setHeight(node.getHeight() + offsety);
//			pos.getPos().x = newPos.x;
//			pos.getPos().y = newPos.y;
//			
//			getResourceProperty().updateOrCreateDoubleProperty(RectangleProperty.NameWidth, node.getWidth());
//			getResourceProperty().updateOrCreateDoubleProperty(RectangleProperty.NameHeight, node.getHeight());
		
		}
		return false;
	}

	@Override
    public TextProperty createResourceProperty() {
		TextProperty prop = new TextProperty();
		prop.fromFnode(this);
		return prop;
    }

	public void load(TextProperty prop) {
		prop.toFnode(this);
		setNodeProperty(prop);
    }
	
	@Override
	protected void setDefaultValue() {
        Object defaultValue = ctx.getDefaultValue(TextProperty.NameFontFamily);
        if(defaultValue != null) {
            setFontFamily(node, defaultValue.toString());
        }
        defaultValue = ctx.getDefaultValue(TextProperty.NameFontSize);
        if(defaultValue != null) {
            node.setFontSize((Integer) defaultValue);
        }
	}
	
	public static void setFontFamily(final Text text, final String fontFamily) {
	    text.setFontFamily(fontFamily);
	    ExternalFunctions.loadFont(fontFamily, null, null);
	    Timer timer = new Timer() {
            @Override
            public void run() {
                text.setFontFamily(fontFamily);
                text.getLayer().draw();
            }
        };
        timer.schedule(500);
	}
}
