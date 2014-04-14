package com.gaoshin.fbobuilder.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Shape.LineJoin;
import net.edzard.kinetic.Text.HorizontalAlignment;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class ResourceProperty {
	private PropertyType type;
	private String name;
	private boolean changed;
	private boolean modifiable;
	private boolean loaded;
	protected List<ResourceProperty> children = new ArrayList<ResourceProperty>();
	private static JSONValue parse;
	
	public ResourceProperty() {
		modifiable = true;
		loaded = true;
    }
	
	public JSONObject toJsonObject(JSONObject subclass) {
		JSONObject obj = null;
		if(subclass != null)
			obj = subclass;
		else
			obj = new JSONObject();
		String clsName = this.getClass().getName();
		clsName = clsName.substring(clsName.lastIndexOf(".")+1);
		obj.put("class", new JSONString(clsName));
		if(name != null)
			obj.put("name", new JSONString(name));
		obj.put("type", new JSONString(type.name()));
		if(children.size() > 0) {
			JSONArray ja = new JSONArray();
			for(ResourceProperty rp : children) {
				ja.set(ja.size(), rp.toJsonObject(null));
			}
			obj.put("children", ja);
		}
		return obj;
	}
	
	public String toJsonString() {
		return toJsonObject(null).toString();
	}
	
	public static ResourceProperty buildResourcePropertyFromJsonString(String json) throws Exception {
		JSONObject obj = (JSONObject) JSONParser.parse(json);
		return buildResourcePropertyFromJsonObject(obj);
	}
	
	public static ResourceProperty buildResourcePropertyFromJsonObject(JSONObject obj) throws Exception {
		JSONValue value = obj.get("class");
		String clsName = ((JSONString)value).stringValue();
		ResourceProperty prop = (ResourceProperty) forName(clsName);
		prop.fromJsonObject(obj);
		return prop;
	}
	
	public void fromJsonObject(JSONObject jobj) throws Exception {
		JSONValue jv = jobj.get("name");
		if(jv != null)
			name = unquote(jv.toString());
		jv = jobj.get("type");
		if(jv != null)
		type = PropertyType.valueOf(unquote(jv.toString()));
		JSONArray jchildren = (JSONArray) jobj.get("children");
		if(jchildren != null) {
			for(int i=0; i<jchildren.size(); i++) {
				JSONObject jo = (JSONObject) jchildren.get(i);
				ResourceProperty property = buildResourcePropertyFromJsonObject(jo);
				children.add(property);
			}
		}
	}
	
	protected void addStrJsonValue(JSONObject obj, String key) {
		String value = getStrProperty(key);
		if(value != null) {
			obj.put(key, new JSONString(value));
		}
	}
	
	protected void addIntJsonValue(JSONObject obj, String key) {
		Integer value = getIntProperty(key);
		if(value != null) {
			obj.put(key, new JSONString(value.toString()));
		}
	}
	
	protected void addDoubleJsonValue(JSONObject obj, String key) {
		Double value = getDoubleProperty(key);
		if(value != null) {
			obj.put(key, new JSONNumber(value));
		}
	}
	
	protected void addColorJsonValue(JSONObject obj, String key) {
		Colour value = getColorProperty(key);
		if(value != null) {
			obj.put(key, new JSONString(value.toString()));
		}
	}
	
	protected void addLineJoinJsonValue(JSONObject obj, String key) {
		LineJoin value = getLineJoinProperty(key);
		if(value != null) {
			obj.put(key, new JSONString(value.toString()));
		}
	}
	
	protected void addTextAlignJsonValue(JSONObject obj, String key) {
		HorizontalAlignment value = getTextAlignProperty(key);
		if(value != null) {
			obj.put(key, new JSONString(value.toString()));
		}
	}
	
	protected void updateOrCreateDoublePropertyFromJson(JSONObject jobj,
            String key) {
		JSONValue jv = jobj.get(key);
		if(jv == null)
			return;
		updateOrCreateDoubleProperty(key, Double.parseDouble(unquote(jv.toString())));
    }

	protected void updateOrCreateIntPropertyFromJson(JSONObject jobj,
            String key) {
		JSONValue jv = jobj.get(key);
		if(jv == null)
			return;
		updateOrCreateIntProperty(key, Integer.parseInt(unquote(jv.toString())));
    }
	
	protected static String unquote(String s) {
		if(s == null) return null;
		if(s.startsWith("\""))
			s = s.substring(1);
		if(s.endsWith("\""))
			s = s.substring(0, s.length()-1);
		return s;
	}

	protected void updateOrCreateColorPropertyFromJson(JSONObject jobj,
            String key) {
		JSONValue jv = jobj.get(key);
		if(jv == null)
			return;
		updateOrCreateColorProperty(key, new Colour(unquote(jv.toString())));
    }

	protected void updateOrCreateLineJoinPropertyFromJson(JSONObject jobj,
            String key) {
		JSONValue jv = jobj.get(key);
		if(jv == null)
			return;
		updateOrCreateLineJoinProperty(key, LineJoin.valueOf(unquote(jv.toString())));
    }

	protected void updateOrCreateTextAlignPropertyFromJson(JSONObject jobj,
            String key) {
		JSONValue jv = jobj.get(key);
		if(jv == null)
			return;
		updateOrCreateTextAlignProperty(key, HorizontalAlignment.valueOf(unquote(jv.toString())));
    }

	protected void updateOrCreateStrPropertyFromJson(JSONObject jobj,
            String key) {
		JSONValue jv = jobj.get(key);
		if(jv == null)
			return;
		updateOrCreateStrProperty(key, unquote(jv.toString()));
    }

	public String getKey() {
		return String.valueOf(hashCode());
	}

	public String getName() {
		return name == null ? type.name() : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ResourceProperty> getChildren() {
		return children;
	}

	public void setChildren(List<ResourceProperty> children) {
		this.children = children;
	}

	public PropertyType getType() {
	    return type;
    }

	public void setType(PropertyType type) {
	    this.type = type;
    }
	
	public void addProperty(ResourceProperty child) {
		children.add(child);
	}
	
	public String getStrProperty(String name) {
		ResourceProperty p = getProperty(name);
		if(p == null) return null;
		StringProperty sp = (StringProperty) p;
		return sp.getValue();
	}
	
	public Double getDoubleProperty(String name) {
		ResourceProperty p = getProperty(name);
		if(p == null) return null;
		DoubleProperty sp = (DoubleProperty) p;
		return sp.getValue();
	}
	
	public Integer getIntProperty(String name) {
		ResourceProperty p = getProperty(name);
		if(p == null) return null;
		IntProperty sp = (IntProperty) p;
		return sp.getValue();
	}
	
	public Integer getZindexProperty() {
		String name = NodeProperty.NameZIndex;
		ResourceProperty p = getProperty(name);
		if(p == null) return null;
		ZindexProperty sp = (ZindexProperty) p;
		return sp.getValue();
	}
	
	public Integer getIntProperty(String name, int defValue) {
		Integer v = getIntProperty(name);
		return v == null ? defValue : v;
	}
	
	public Colour getColorProperty(String name) {
		ResourceProperty p = getProperty(name);
		if(p == null) return null;
		ColorProperty sp = (ColorProperty) p;
		return sp.getValue();
	}
	
	public LineJoin getLineJoinProperty(String name) {
		ResourceProperty p = getProperty(name);
		if(p == null) return null;
		LineJoinProperty sp = (LineJoinProperty) p;
		return sp.getValue();
	}
	
	public HorizontalAlignment getTextAlignProperty(String name) {
		ResourceProperty p = getProperty(name);
		if(p == null) return null;
		TextAlignProperty sp = (TextAlignProperty) p;
		return sp.getValue();
	}
	
	public ResourceProperty getProperty(String name) {
		for(ResourceProperty rp : children) {
			if(rp.getName().equals(name))
				return rp;
		}
		return null;
	}
	
	
	public void removeProperty(String name) {
		for(int i=children.size()-1; i>=0; i--) {
			if(children.get(i).getName().equals(name)) {
				children.remove(i);
				return;
			}
		}
	}
	
	public void addIntProperty(String name, int value) {
		ResourceProperty prop = getProperty(name);
		IntProperty child = null;
		if(prop == null || !(prop instanceof IntProperty))
			child = new IntProperty();
		else
			child = (IntProperty) prop;
		child.setName(name);
		child.setValue(value);
		addProperty(child);
	}
	
	public void addZindexProperty(int value) {
		String name = NodeProperty.NameZIndex;
		ResourceProperty prop = getProperty(name);
		ZindexProperty child = null;
		if(prop == null || !(prop instanceof ZindexProperty))
			child = new ZindexProperty();
		else
			child = (ZindexProperty) prop;
		child.setName(name);
		child.setValue(value);
		addProperty(child);
	}
	
	public void addDoubleProperty(String name, double value) {
		ResourceProperty prop = getProperty(name);
		DoubleProperty child = null;
		if(prop == null || !(prop instanceof DoubleProperty)) {
			child = new DoubleProperty();
			child.setName(name);
			child.setValue(value);
			addProperty(child);
		}
		else {
			child = (DoubleProperty) prop;
			child.setName(name);
			child.setValue(value);
		}
	}
	
	public void addColorProperty(String name, Colour value) {
		ResourceProperty prop = getProperty(name);
		ColorProperty child = null;
		if(prop == null || !(prop instanceof ColorProperty))
			child = new ColorProperty();
		else
			child = (ColorProperty) prop;
		child.setName(name);
		child.setValue(value);
		addProperty(child);
	}
	
	public void addLineJoinProperty(String name, LineJoin value) {
		ResourceProperty prop = getProperty(name);
		LineJoinProperty child = null;
		if(prop == null || !(prop instanceof LineJoinProperty))
			child = new LineJoinProperty();
		else
			child = (LineJoinProperty) prop;
		child.setName(name);
		child.setValue(value);
		addProperty(child);
	}
	
	public void addTextAlignProperty(String name, HorizontalAlignment value) {
		ResourceProperty prop = getProperty(name);
		TextAlignProperty child = null;
		if(prop == null || !(prop instanceof TextAlignProperty))
			child = new TextAlignProperty();
		else
			child = (TextAlignProperty) prop;
		child.setName(name);
		child.setValue(value);
		addProperty(child);
	}
	
	public void addStrProperty(String name, String value) {
		StringProperty child = new StringProperty();
		child.setName(name);
		child.setValue(value);
		addProperty(child);
	}
	
	public void updateOrCreateDoubleProperty(String name, double value) {
		ResourceProperty prop = getProperty(name);
		DoubleProperty child = null;
		if(prop == null || !(prop instanceof DoubleProperty)) {
			child = new DoubleProperty();
			addProperty(child);
		}
		else
			child = (DoubleProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public void updateOrCreateStrProperty(String name, String value) {
		ResourceProperty prop = getProperty(name);
		StringProperty child = null;
		if(prop == null || !(prop instanceof StringProperty)) {
			child = new StringProperty();
			addProperty(child);
		}
		else
			child = (StringProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public void updateOrCreateColorProperty(String name, Colour value) {
		ResourceProperty prop = getProperty(name);
		ColorProperty child = null;
		if(prop == null || !(prop instanceof ColorProperty)) {
			child = new ColorProperty();
			addProperty(child);
		}
		else
			child = (ColorProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public void updateOrCreateLineJoinProperty(String name, LineJoin value) {
		ResourceProperty prop = getProperty(name);
		LineJoinProperty child = null;
		if(prop == null || !(prop instanceof LineJoinProperty)) {
			child = new LineJoinProperty();
			addProperty(child);
		}
		else
			child = (LineJoinProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public void updateOrCreateTextAlignProperty(String name, HorizontalAlignment value) {
		ResourceProperty prop = getProperty(name);
		TextAlignProperty child = null;
		if(prop == null || !(prop instanceof TextAlignProperty)) {
			child = new TextAlignProperty();
			addProperty(child);
		}
		else
			child = (TextAlignProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public void updateOrCreateFontFamilyProperty(String name, String value) {
		ResourceProperty prop = getProperty(name);
		FontFamilyProperty child = null;
		if(prop == null || !(prop instanceof FontFamilyProperty)) {
			child = new FontFamilyProperty();
			addProperty(child);
		}
		else
			child = (FontFamilyProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public void updateOrCreateIntProperty(String name, int value) {
		ResourceProperty prop = getProperty(name);
		IntProperty child = null;
		if(prop == null || !(prop instanceof IntProperty)) {
			child = new IntProperty();
			addProperty(child);
		}
		else
			child = (IntProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public void updateOrCreateZindexProperty(int value) {
		String name = NodeProperty.NameZIndex;
		ResourceProperty prop = getProperty(name);
		ZindexProperty child = null;
		if(prop == null || !(prop instanceof ZindexProperty)) {
			child = new ZindexProperty();
			addProperty(child);
		}
		else
			child = (ZindexProperty) prop;
		child.setName(name);
		child.setValue(value);
		child.setChanged(true);
	}

	public boolean isChanged() {
	    return changed;
    }

	public void setChanged(boolean changed) {
	    this.changed = changed;
    }
	
	public boolean equals(Object o) {
		if(o == null) return false;
		return this.hashCode() == o.hashCode();
	}

	public boolean isModifiable() {
	    return modifiable;
    }

	public void setModifiable(boolean modifiable) {
	    this.modifiable = modifiable;
    }
	
	public static Object forName(String clsName) {
		clsName = unquote(clsName);
		if(clsName.equals("AudioProperty")) return new AudioProperty();
		if(clsName.equals("CircleProperty")) return new CircleProperty();
		if(clsName.equals("ColorProperty")) return new ColorProperty();
		if(clsName.equals("LineJoinProperty")) return new LineJoinProperty();
		if(clsName.equals("TextAlignProperty")) return new TextAlignProperty();
		if(clsName.equals("ContainerProperty")) return new ContainerProperty();
		if(clsName.equals("DoubleProperty")) return new DoubleProperty();
		if(clsName.equals("EllipseProperty")) return new EllipseProperty();
		if(clsName.equals("FlyerFolderProperty")) return new FlyerFolderProperty();
		if(clsName.equals("FlyerProperty")) return new FlyerProperty();
		if(clsName.equals("FontFamilyProperty")) return new FontFamilyProperty();
		if(clsName.equals("ImageProperty")) return new ImageProperty();
		if(clsName.equals("IntProperty")) return new IntProperty();
		if(clsName.equals("IntegerListProperty")) return new IntegerListProperty();
		if(clsName.equals("LayerProperty")) return new LayerProperty();
		if(clsName.equals("LineProperty")) return new LineProperty();
		if(clsName.equals("NodeProperty")) return new NodeProperty();
		if(clsName.equals("PathSVGProperty")) return new PathSVGProperty();
		if(clsName.equals("PolygonProperty")) return new PolygonProperty();
		if(clsName.equals("RectangleProperty")) return new RectangleProperty();
		if(clsName.equals("RegularPolygonProperty")) return new RegularPolygonProperty();
		if(clsName.equals("ResourceProperty")) return new ResourceProperty();
		if(clsName.equals("ShapeProperty")) return new ShapeProperty();
		if(clsName.equals("SpriteProperty")) return new SpriteProperty();
		if(clsName.equals("StageProperty")) return new StageProperty();
		if(clsName.equals("StarProperty")) return new StarProperty();
		if(clsName.equals("StringProperty")) return new StringProperty();
		if(clsName.equals("TextPathProperty")) return new TextPathProperty();
		if(clsName.equals("TextProperty")) return new TextProperty();
		if(clsName.equals("FileProperty")) return new FileProperty();
		if(clsName.equals("ZindexProperty")) return new ZindexProperty();
		return null;
	}

	public boolean isLoaded() {
	    return loaded;
    }

	public void setLoaded(boolean loaded) {
	    this.loaded = loaded;
    }

	public void sortByZindex() {
		Collections.sort(children, new Comparator<ResourceProperty>() {
			@Override
            public int compare(ResourceProperty o1, ResourceProperty o2) {
				int z1 = o1.getIntProperty(NodeProperty.NameZIndex, -1);
				int z2 = o2.getIntProperty(NodeProperty.NameZIndex, -1);
	            return z1 - z2;
            }
		});
		
		for (ResourceProperty rp : children) {
			rp.sortByZindex();
		}
    }

	public void removeChild(ResourceProperty sp) {
		for(int i=children.size()-1; i>=0; i--) {
			ResourceProperty child = children.get(i);
			if(child.equals(sp)) {
				children.remove(i);
				return;
			}
		}
	}
}
