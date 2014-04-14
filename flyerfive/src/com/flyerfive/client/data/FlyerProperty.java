package com.flyerfive.client.data;

import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.model.Fstage;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class FlyerProperty extends ContainerProperty<Fstage> {
	public static final String NameFlyerName = "flyer.name";
	public static final String NameFlyerWidth = "flyer.width";
	public static final String NameFlyerHeight = "flyer.height";
	
	private int version;
	private String description;
	private String id;
	private String permission = "700";

	public FlyerProperty() {
		setType(PropertyType.Flyer);
		setName("Flyer");
		setModifiable(false);
    }

	public JSONObject toJsonObject(JSONObject subclass) {
		JSONObject obj = null;
		if(subclass != null)
			obj = subclass;
		else
			obj = new JSONObject();
		super.toJsonObject(obj);
		if(id != null)
			obj.put("id", new JSONString(id));
		if(description != null)
			obj.put("description", new JSONString(description));
		obj.put("version", new JSONNumber(version));
		return obj;
	}
	
	public void fromJsonObject(JSONObject jobj) throws Exception {
		super.fromJsonObject(jobj);
		JSONValue jv = jobj.get("id");
		if(jv != null)
			id = unquote(jv.toString());
		jv = jobj.get("description");
		if(jv != null)
			description = unquote(jv.toString());
		jv = jobj.get("version");
		if(jv != null)
			version = Integer.parseInt(unquote(jv.toString()));
	}


	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}

	public String getDescription() {
	    return description;
    }

	public void setDescription(String description) {
	    this.description = description;
    }
	
	public Double getWidth() {
		return getDoubleProperty(NameFlyerWidth);
	}
	
	public Double getHeight() {
		return getDoubleProperty(NameFlyerHeight);
	}
	
	public int getIntWidth() {
		return getDoubleProperty(NameFlyerWidth).intValue();
	}
	
	public int getIntHeight() {
		return getDoubleProperty(NameFlyerHeight).intValue();
	}
	
	@Override
	public boolean updateView(Fstage fnode, PrimeProperty rp) {
	    boolean found = super.updateView(fnode, rp);
	    if(found) return found;
	    
	    if(rp.getName().endsWith(NameFlyerWidth)) {
		    double w = ((DoubleProperty)rp).getValue();
		    Vector2d size = fnode.getNode().getSize();
		    size.x = w;
		    fnode.getNode().setSize(size);
		    found = true;
	    }
	    
	    if(rp.getName().endsWith(NameFlyerHeight)) {
		    double h = ((DoubleProperty)rp).getValue();
		    Vector2d size = fnode.getNode().getSize();
		    size.y = h;
		    fnode.getNode().setSize(size);
		    found = true;
	    }
	    
	    if(rp.getName().endsWith(NameFlyerName)) {
	    	String newName = ((StringProperty)rp).getValue();
			setName(newName);
	    	fnode.getNode().setName(newName);
		    found = true;
	    }
	    
	    return found;
	}
	
	@Override
	public void fromFnode(Fstage fnode) {
	    super.fromFnode(fnode);
	    Vector2d size = fnode.getNode().getSize();
	    updateOrCreateDoubleProperty(NameFlyerWidth, size.x);
	    updateOrCreateDoubleProperty(NameFlyerHeight, size.y);
	    updateOrCreateStrProperty(NameFlyerName, fnode.getNode().getName());
	    setId(fnode.getNode().getID());
	}

	public String getId() {
	    return id;
    }

	public void setId(String id) {
	    this.id = id;
    }

	public String getPermission() {
	    return permission;
    }

	public void setPermission(String permission) {
	    this.permission = permission;
    }
	
	public void setPublic() {
		permission = "777";
	}
	
	public void setPrivate() {
		permission = "700";
	}
	
	public boolean isPublic() {
		return "777".equals(permission);
	}
}
