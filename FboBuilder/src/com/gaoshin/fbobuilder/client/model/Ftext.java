package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Text;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.NodeProperty;
import com.gaoshin.fbobuilder.client.data.TextProperty;
import com.gaoshin.fbobuilder.client.model.Controller.ControllerAnchor;

public class Ftext extends Fshape<Text, TextProperty> {
	public Ftext(Flayer flayer, TextProperty tp) {
	    super(flayer, tp);
    }

	@Override
    protected Text createKineticNode() {
	    Vector2d position = nearTopLeft();
		String aText = "Hello world!";
		if(initNodeProperty != null) {
			aText = initNodeProperty.getStrProperty(TextProperty.NameText);
		}
		Text text = Kinetic.createText(position, aText );
		text.setFontSize(36);
		text.setFontFamily("Tangerine");
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
}
