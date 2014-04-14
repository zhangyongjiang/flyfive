package com.gaoshin.fbobuilder.client.data;

import net.edzard.kinetic.Colour;
import net.edzard.kinetic.FillStyle;
import net.edzard.kinetic.Shadow;
import net.edzard.kinetic.Shape;
import net.edzard.kinetic.Shape.LineJoin;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.model.Fshape;
import com.google.gwt.json.client.JSONObject;

public class ShapeProperty<T extends Fshape> extends NodeProperty<T> {
	public static final String NameShadowColor = "shadow color";
	public static final String NameShadowOffsetX = "shadow.offset.x";
	public static final String NameShadowOffsetY = "shadow.offset.y";
	public static final String NameStrokeColor = "stroke color";
	public static final String NameStrokeWidth = "stroke.width";
	public static final String NameFillColor= "fill color";
	public static final String NameLineJoin = "line join";
	
	public ShapeProperty() {
		setType(PropertyType.Shape);
    }
	
	public void fromFnode(T fshape) {
		super.fromFnode(fshape);
		Shape shape = (Shape) fshape.getNode();
		Shadow shadow = shape.getShadow();
		if(shadow != null) {
			addColorProperty(NameShadowColor, shadow.getColour());
			updateOrCreateDoubleProperty(NameShadowOffsetX, shadow.getOffset().x);
			updateOrCreateDoubleProperty(NameShadowOffsetY, shadow.getOffset().y);
		}
		else {
			addColorProperty(NameShadowColor, Colour.white);
			updateOrCreateDoubleProperty(NameShadowOffsetX, 0);
			updateOrCreateDoubleProperty(NameShadowOffsetY, 0);
		}

		try {
			Colour stroke = shape.getStroke();
			addColorProperty(NameStrokeColor, stroke);
		}
		catch (Exception e) {
		}
		
		double strokeWidth = shape.getStrokeWidth();
		updateOrCreateDoubleProperty(NameStrokeWidth, strokeWidth);
		
		LineJoin join = shape.getLineJoin();
		if(join == null) {
			join = LineJoin.BEVEL;
		}
		updateOrCreateLineJoinProperty(NameLineJoin, join);
		
		FillStyle fill = shape.getFill();
		if(fill != null && fill instanceof Colour) {
			Colour colr = (Colour) fill;
			addColorProperty(NameFillColor, colr);
		}
	}
	
	@Override
	public void toFnode(T fnode) {
	    super.toFnode(fnode);
		Shape shape = (Shape) fnode.getNode();
		
		{
		    ResourceProperty property = getProperty(NameShadowColor);
			Colour colr = getColorProperty(NameShadowColor);
		    if(property != null && colr != null) {
				Shadow shadow = shape.getShadow();
				if(shadow == null)
					shadow = new Shadow();
				shadow.setColour(colr);
				shape.setShadow(shadow);
		    }
		}
		
		{
		    ResourceProperty property = getProperty(NameShadowOffsetX);
		    if(property != null) {
				Shadow shadow = shape.getShadow();
				if(shadow == null)
					shadow = new Shadow();
				Vector2d offset = shadow.getOffset();
				offset.x = getDoubleProperty(NameShadowOffsetX);
				shadow.setOffset(offset);
				shape.setShadow(shadow);
		    }
		}
		
		{
		    ResourceProperty property = getProperty(NameShadowOffsetY);
		    if(property != null) {
				Shadow shadow = shape.getShadow();
				if(shadow == null)
					shadow = new Shadow();
				Vector2d offset = shadow.getOffset();
				offset.y = getDoubleProperty(NameShadowOffsetY);
				shadow.setOffset(offset);
				shape.setShadow(shadow);
		    }
		}
		
		{
		    ResourceProperty property = getProperty(NameStrokeColor);
		    if(property != null) {
				Colour colr = getColorProperty(NameStrokeColor);
				shape.setStroke(colr);
				shape.setStrokeWidth(getDoubleProperty(NameStrokeWidth));
		    }
		}
		
		{
		    ResourceProperty property = getProperty(NameFillColor);
		    if(property != null) {
				Colour colr = getColorProperty(NameFillColor);
				shape.setFill(colr);
		    }
		}
		
		{
		    ResourceProperty property = getProperty(NameLineJoin);
		    if(property != null) {
		    	LineJoin join = getLineJoinProperty(NameLineJoin);
		    	if(join == null)
		    		join = LineJoin.BEVEL;
				shape.setLineJoin(join);
		    }
		}
		
		{
		    ResourceProperty property = getProperty(NameUrl);
		    if(property != null) {
				String url = getStrProperty(NameUrl);
				fnode.setUrl(url);
		    }
		}
	}
	
	@Override
	public boolean updateView(T fnode, PrimeProperty rp) {
		boolean found = super.updateView(fnode, rp);
		if(found) return true;
		
		Shape shape = (Shape)fnode.getNode();
		if(rp.getName().equals(ShapeProperty.NameStrokeWidth)) {
			shape.setStrokeWidth(((DoubleProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(ShapeProperty.NameStrokeColor)) {
			shape.setStroke(((ColorProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(ShapeProperty.NameFillColor)) {
			shape.setFill(((ColorProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(ShapeProperty.NameLineJoin)) {
			LineJoinProperty sp = (LineJoinProperty)rp;
			LineJoin join = sp.getValue();
			shape.setLineJoin(join);
			found = true;
		}
		else if(rp.getName().equals(ShapeProperty.NameShadowColor)) {
			Colour value = ((ColorProperty)rp).getValue();
			Shadow shadow = shape.getShadow();
			if(shadow == null)
				shadow = new Shadow();
			shadow.setColour(value);
			shape.setShadow(shadow);
			found = true;
		}
		else if(rp.getName().equals(ShapeProperty.NameShadowOffsetX)) {
			Double value = ((DoubleProperty)rp).getValue();
			Shadow shadow = shape.getShadow();
			if(shadow == null)
				shadow = new Shadow();
			shadow.setOffset(new Vector2d(value, 0));
			shape.setShadow(shadow);
			found = true;
		}
		else if(rp.getName().equals(ShapeProperty.NameShadowOffsetY)) {
			Double value = ((DoubleProperty)rp).getValue();
			Shadow shadow = shape.getShadow();
			if(shadow == null)
				shadow = new Shadow();
			shadow.setOffset(new Vector2d(0, value));
			shape.setShadow(shadow);
			found = true;
		}
		return found;
	}
	
	@Override
	public void fromJsonObject(JSONObject jobj) throws Exception {
	    super.fromJsonObject(jobj);
	    if(getProperty(NameUrl) == null) {
	    	updateOrCreateStrProperty(NameUrl, null);
	    }
	}
}
