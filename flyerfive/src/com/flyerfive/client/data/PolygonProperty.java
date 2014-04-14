package com.flyerfive.client.data;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Polygon;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.model.Fpolygon;

public class PolygonProperty extends ShapeProperty<Fpolygon> {
	public static final String NamePoints = "points";
	public static final String NameSides = "sides";
	
	public PolygonProperty() {
		setType(PropertyType.Polygon);
    }
	
	@Override
	public void fromFnode(Fpolygon fshape) {
	    super.fromFnode(fshape);
		updateOrCreateStrProperty(NamePoints, getPointsFromNode(fshape));
	}
	
	public static String getPointsFromNode(Fpolygon fshape) {
	    List<Vector2d> points = fshape.getNode().getPoints();
	    StringBuilder sb = new StringBuilder();
	    for(Vector2d v : points) {
	    	sb.append(v.x).append(",").append(v.y).append(";");
	    }
	    return sb.substring(0,  sb.length()-1);
	}
	
	public static List<Vector2d> getPointsFromStr(String strPoints) {
	    List<Vector2d> points = new ArrayList<Vector2d>();
	    for(String point : strPoints.split(";")) {
	    	String[] split = point.split(",");
	    	Vector2d v = new Vector2d(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
	    	points.add(v);
	    }
	    return points;
	}
	
	@Override
	public void toFnode(Fpolygon fnode) {
	    super.toFnode(fnode);
	    String strPoints = getStrProperty(NamePoints);
	    fnode.getNode().setPoints(getPointsFromStr(strPoints));
	}
	
	public void updateResourceProperty(Fpolygon fnode, String... keys) {
		super.updateResourceProperty(fnode, keys);
		for(String key : keys) {
			if(key.equals(NamePoints)) {
				updateOrCreateStrProperty(NamePoints, getPointsFromNode(fnode));
			}
		}
	}
	
	@Override
	public boolean updateView(Fpolygon fnode, PrimeProperty rp) {
	    boolean found = super.updateView(fnode, rp);
	    if(found) return true;
		
		Polygon shape = (Polygon)fnode.getNode();
		if(rp.getName().equals(PolygonProperty.NamePoints)) {
			shape.setPoints(getPointsFromStr(getStrProperty(NamePoints)));
			found = true;
		}
	    return found;
	}
}
