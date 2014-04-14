package com.flyerfive.client.model;

import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Event;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Shadow;
import net.edzard.kinetic.Shape;
import net.edzard.kinetic.Shape.LineJoin;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.ShapeProperty;
import com.google.gwt.dom.client.Style.Cursor;

public abstract class Fshape<T extends Shape, P extends ShapeProperty> extends Fnode<T, P> {
	private boolean eventListenerAdded = false;

	public Fshape(Flayer flayer, P sp, FlyerContext cxt) {
	    super(flayer, sp, cxt);
    }

	public Fshape(Flayer flayer, T t) {
	    super(flayer, t);
    }

	@Override
	protected void setupDisplayModeMouseEvent() {
		if(getUrl() == null || (DisplayMode.Edit.equals(getFstage().getMode()))) 
			return;
		if(eventListenerAdded)
			return;
		eventListenerAdded = true;
		node.addEventListener(Event.Type.MOUSEOVER, new Node.EventListener() {
			@Override
			public boolean handle(Event evt) {
				node.getLayer().getCanvas().getElement().getParentElement().getStyle().setCursor(Cursor.POINTER);
				Shadow shadow = new Shadow(Colour.aqua, 1, new Vector2d(2,2), 1);
				node.setShadow(shadow);
				flayer.getNode().draw();
				return true;
			}
		});
		node.addEventListener(Event.Type.MOUSEOUT, new Node.EventListener() {
			@Override
			public boolean handle(Event evt) {
				node.getLayer().getCanvas().getElement().getParentElement().getStyle().setCursor(Cursor.DEFAULT);
				Colour colour = getResourceProperty().getColorProperty(ShapeProperty.NameShadowColor);
				Double x = getResourceProperty().getDoubleProperty(ShapeProperty.NameShadowOffsetX);
				Double y = getResourceProperty().getDoubleProperty(ShapeProperty.NameShadowOffsetY);
				node.setShadow(new Shadow(colour, 1, new Vector2d(x, y), 1));
				flayer.getNode().draw();
				return true;
			}
		});
		node.addEventListener(Event.Type.MOUSEUP, new Node.EventListener() {
			@Override
			public synchronized boolean handle(Event evt) {
				if(getUrl() != null) {
					openUrl();
				}
				return true;
			}
		});
	}
	
	@Override
	protected void setDefaultValue() {
	    super.setDefaultValue();
        
        Object defaultValue = ctx.getDefaultValue(ShapeProperty.NameFillColor);
        if(defaultValue != null) {
            node.setFill(new Colour(defaultValue.toString()));
        }
        
        defaultValue = ctx.getDefaultValue(ShapeProperty.NameShadowColor);
        if(defaultValue != null) {
            Shadow shadow = new Shadow();
            shadow.setColour(new Colour(defaultValue.toString()));
            node.setShadow(shadow);
        }
        
        defaultValue = ctx.getDefaultValue(ShapeProperty.NameStrokeColor);
        if(defaultValue != null) {
            node.setStroke(new Colour(defaultValue.toString()));
        }
        
        defaultValue = ctx.getDefaultValue(ShapeProperty.NameStrokeWidth);
        if(defaultValue != null) {
            node.setStrokeWidth((Double) defaultValue);
        }
        else {
            node.setStrokeWidth(getStageSize().x / 100);
        }
        
        defaultValue = ctx.getDefaultValue(ShapeProperty.NameLineJoin);
        if(defaultValue != null) {
            node.setLineJoin( (LineJoin) defaultValue);
        }
	}
}
