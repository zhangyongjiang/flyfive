package com.flyerfive.client.data;

import net.edzard.kinetic.Node;

import com.flyerfive.client.model.Fnode;
import com.flyerfive.client.model.Fstage;
import com.google.gwt.json.client.JSONObject;

public class NodeProperty<T extends Fnode> extends ResourceProperty {
	public static final String NamePositionX = "position.x";
	public static final String NamePositionY = "position.y";
	public static final String NameRotation = "rotation";
	public static final String NameOpacity = "opacity";
	public static final String NameZIndex = "zindex";
    public static final String NameUrl = "url";
    public static final String NameId = "id";
	
	public NodeProperty() {
	    setType(PropertyType.Node);
    }
	
	public void fromFnode(T fnode) {
	    Node node = fnode.getNode();
	    
	    if(!(fnode instanceof Fstage)) { 
	    	addZindexProperty(node.getZIndex());
		    updateOrCreateDoubleProperty(NamePositionX, node.getX());
		    updateOrCreateDoubleProperty(NamePositionY, node.getY());
		    updateOrCreateDoubleProperty(NameRotation, node.getRotation());
		    updateOrCreateDoubleProperty(NameOpacity, node.getOpacity());
	    }
	    
	    String id = fnode.getNode().getID();
	    if(id != null)
	        updateOrCreateStrProperty(NameId, id);
	    updateOrCreateStrProperty(NameUrl, fnode.getUrl());
	}
	
	public void toFnode(T fnode) {
	    Node node = fnode.getNode();
	    node.setZIndex(getZindexProperty());
	    node.setX(getDoubleProperty(NamePositionX));
	    node.setY(getDoubleProperty(NamePositionY));
	    node.setRotation(getDoubleProperty(NameRotation));
        node.setOpacity(getDoubleProperty(NameOpacity));
        node.setID(getStrProperty(NameId));
	    fnode.setUrl(getStrProperty(NameUrl));
	}
	
	public <T extends Fnode> void updateResourceProperty(T fnode, String... keys) {
		for(String key : keys) {
			if(key.equals(NamePositionX)) {
				updateOrCreateDoubleProperty(NamePositionX, fnode.getNode().getX());
			}
			if(key.equals(NamePositionY))
				updateOrCreateDoubleProperty(NamePositionY, fnode.getNode().getY());
			if(key.equals(NameRotation))
				updateOrCreateDoubleProperty(NameRotation, fnode.getNode().getRotation());
			if(key.equals(NameOpacity))
				updateOrCreateDoubleProperty(NameOpacity, fnode.getNode().getOpacity());
            if(key.equals(NameUrl))
                updateOrCreateStrProperty(NameUrl, fnode.getUrl());
            if(key.equals(NameId))
                updateOrCreateStrProperty(NameId, fnode.getNode().getID());
		}
	}

	public boolean updateView(T fnode, PrimeProperty rp) {
		Node node = fnode.getNode();
		boolean found = false;
		if(rp.getName().equals(NodeProperty.NamePositionX)) {
			node.setX(((DoubleProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(NodeProperty.NamePositionY)) {
			node.setY(((DoubleProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(NodeProperty.NameRotation)) {
			node.setRotation(((DoubleProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(NodeProperty.NameZIndex)) {
			node.setZIndex(((ZindexProperty)rp).getValue());
			found = true;
		}
		else if(rp.getName().equals(NodeProperty.NameOpacity)) {
			node.setOpacity(((DoubleProperty)rp).getValue());
			found = true;
		}
		return found;
    }
	
	@Override
	public void fromJsonObject(JSONObject jobj) throws Exception {
	    super.fromJsonObject(jobj);
	}
}
