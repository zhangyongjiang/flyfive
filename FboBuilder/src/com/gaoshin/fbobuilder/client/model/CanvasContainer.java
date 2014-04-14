package com.gaoshin.fbobuilder.client.model;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Stage;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.DoubleProperty;
import com.gaoshin.fbobuilder.client.data.FlyerProperty;
import com.gaoshin.fbobuilder.client.data.LayerProperty;
import com.gaoshin.fbobuilder.client.data.NodeProperty;
import com.gaoshin.fbobuilder.client.data.PrimeProperty;
import com.gaoshin.fbobuilder.client.data.ResourceProperty;
import com.gaoshin.fbobuilder.client.data.ShapeProperty;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;

public class CanvasContainer {
	private Fstage fstage;
	private DisplayMode mode = DisplayMode.Edit;
	private Element element;
	
	public void setContainer(Element element) {
		this.element = element;
    }
	
	public void setContainerId(String elementId) {
		this.element = Document.get().getElementById(elementId);
    }
	
	public void createStage(int width, int height) {
		element.getStyle().setWidth(width, Unit.PX);
		element.getStyle().setHeight(height, Unit.PX);
		if(fstage == null) {
			Stage stage = Kinetic.createStage(element, width, height);
			this.fstage = new Fstage(stage, null);
			fstage.setMode(mode);
			fstage.draw();
		}
		else {
			reset();
			fstage.setNodeProperty(null);
			FlyerProperty fp = fstage.getResourceProperty();
			fp.updateOrCreateDoubleProperty(FlyerProperty.NameFlyerWidth, (double)width);
			fp.updateOrCreateDoubleProperty(FlyerProperty.NameFlyerHeight, (double)height);
			fstage.getNode().setWidth(width);
			fstage.getNode().setHeight(height);
		}
	}

	public void loadJson(String json) throws Exception {
		FlyerProperty data = (FlyerProperty) ResourceProperty.buildResourcePropertyFromJsonString(json);
		load(data);
	}

	public void load(FlyerProperty data) {
		if(fstage == null) {
			Stage stage = Kinetic.createStage(element, data.getWidth().intValue(), data.getHeight().intValue());
			this.fstage = new Fstage(stage, null);
			fstage.setMode(mode);
		}
		fstage.load(data);
		element.getStyle().setWidth(data.getIntWidth(), Unit.PX);
		element.getStyle().setHeight(data.getIntHeight(), Unit.PX);
    }

	public FlyerProperty getData() {
		if(fstage == null)
			return null;
		else
			return fstage.getResourceProperty();
	}

    public boolean updatePrimeProperty(NodeProperty parent, PrimeProperty rp) {
		boolean result = fstage.updatePrimeProperty(parent, rp);
		if(parent.equals(fstage.getResourceProperty())) {
			if(rp.getName().equals(FlyerProperty.NameFlyerWidth)) {
			    double w = ((DoubleProperty)rp).getValue();
			    element.getStyle().setWidth(w, Unit.PX);
			}
			else if(rp.getName().equals(FlyerProperty.NameFlyerHeight)) {
			    double h = ((DoubleProperty)rp).getValue();
			    element.getStyle().setHeight(h, Unit.PX);
			}
		}
		return result;
    }
    
	public void selectNode(ResourceProperty nodeProperty) {
		fstage.selectNode(nodeProperty);
    }

    public void reset() {
		if(fstage != null)
			fstage.reset();
    }

	public void deleteShape(ShapeProperty sp) {
		fstage.deleteShape(sp);
    }

	public Flayer createLayer() {
	    return fstage.createLayer();
    }

	public Fnode createNewNode(String class1, LayerProperty lp, NodeProperty np) {
		return fstage.createNewNode(class1, lp, np);
    }

	public boolean moveDown(LayerProperty layer, NodeProperty node) {
		return fstage.moveDown(layer, node);
    }

	public boolean moveUp(LayerProperty layer, NodeProperty node) {
		return fstage.moveUp(layer, node);
    }

	public void setDisplayMode(DisplayMode mode) {
		this.mode  = mode;
		if(fstage != null)
			fstage.setMode(mode);
	}
	
	public void getImage(final StageDataCallback callback) {
		fstage.getImage(new net.edzard.kinetic.Stage.DataUrlTarget(){
			@Override
            public void receive(String url) {
				callback.receive(url);
            }});
	}

	public void onFontsLoaded() {
		try {
            load(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void onImgLoaded() {
		if(fstage != null)
			fstage.onImgLoaded();
    }

	public void zoomIn(float scaleValue) {
		fstage.onZoomin(scaleValue);
		Vector2d scale = fstage.getNode().getScale();
		element.getStyle().setWidth(fstage.getResourceProperty().getIntWidth() * scale.x, Unit.PX);
		element.getStyle().setHeight(fstage.getResourceProperty().getIntHeight() * scale.y, Unit.PX);
    }

	public void zoomOut(float scaleValue) {
		fstage.onZoomout(scaleValue);
		Vector2d scale = fstage.getNode().getScale();
		element.getStyle().setWidth(fstage.getResourceProperty().getIntWidth() * scale.x, Unit.PX);
		element.getStyle().setHeight(fstage.getResourceProperty().getIntHeight() * scale.y, Unit.PX);
    }
}
