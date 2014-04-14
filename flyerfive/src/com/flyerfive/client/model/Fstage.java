package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Stage;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.DoubleProperty;
import com.flyerfive.client.data.FlyerProperty;
import com.flyerfive.client.data.LayerProperty;
import com.flyerfive.client.data.NodeProperty;
import com.flyerfive.client.data.PrimeProperty;
import com.flyerfive.client.data.ResourceProperty;
import com.flyerfive.client.data.ShapeProperty;
import com.flyerfive.client.data.StringProperty;
import com.flyerfive.client.message.MsgHandler;
import com.flyerfive.client.message.SelectNextNodeMsg;
import com.flyerfive.client.message.SelectPrevNodeMsg;
import com.google.gwt.user.client.Window;

public class Fstage extends Fcontainer<Stage, FlyerProperty>{
	private List<Flayer> flayers = new ArrayList<Flayer>();
	private int currentLayer;
	private Flayer controllerLayer;
	private Controller controller;
	private DisplayMode mode = DisplayMode.Edit;
	
	public Fstage(Stage stage, FlyerProperty fp, FlyerContext cxt) {
		super(null, fp, cxt);
	    this.node = stage;
	    currentLayer = -1;
	    
	    setupMsgHandler();
    }
	
	private void setupMsgHandler() {
        ctx.getMb().registerHandler(SelectPrevNodeMsg.class, new MsgHandler<SelectPrevNodeMsg>() {
            @Override
            public void processMsg(SelectPrevNodeMsg msg) {
                selectPrevNode();
            }
        });
        ctx.getMb().registerHandler(SelectNextNodeMsg.class, new MsgHandler<SelectNextNodeMsg>() {
            @Override
            public void processMsg(SelectNextNodeMsg msg) {
                selectNextNode();
            }
        });
    }

    protected void selectNextNode() {
        Fnode target = controller.getTarget();
        if(target == null)
            return;
        target.getFlayer().selectNextNode(target);
    }

    protected void selectPrevNode() {
        Fnode target = controller.getTarget();
        if(target == null)
            return;
        target.getFlayer().selectPrevNode(target);
    }

    public Stage getStage() {
		return node;
	}
	
	public void draw() {
		getStage().draw();
    }
	
    public void onZoomout(float scaleValue) {
		Vector2d scale = getStage().getScale();
		scale.x = scale.x / scaleValue;
		scale.y = scale.y / scaleValue;
		getStage().setScale(scale);
		int w = getResourceProperty().getIntWidth();
		int h = getResourceProperty().getIntHeight();
		getStage().setSize(new Vector2d(w * scale.x, h * scale.y));
		getStage().draw();
    }

    public void onZoomin(float scaleValue) {
		Vector2d scale = getStage().getScale();
		scale.x = scale.x * scaleValue;
		scale.y = scale.y * scaleValue;
		getStage().setScale(scale);
		int w = getResourceProperty().getIntWidth();
		int h = getResourceProperty().getIntHeight();
		getStage().setSize(new Vector2d(w * scale.x, h * scale.y));
		getStage().draw();
    }
    
	public Flayer getCurrentLayer() {
	    return currentLayer < 0 ? null : flayers.get(currentLayer);
    }

	public Fnode createNewNode(String class1, LayerProperty lp, NodeProperty np) {
		for(int i=0; i<flayers.size(); i++) {
			Flayer fl = flayers.get(i);
			if(fl.getResourceProperty().equals(lp)) {
				currentLayer = i;
			}
		}
		if(currentLayer < 0) {
			createLayer();
		}
		Fnode fnode = flayers.get(currentLayer).createNewNode(class1, np);
		draw();
		return fnode;
    }
	
	public void reset() {
		if(getStage() != null) {
			getStage().removeChildren();
			getStage().setID(null);
			getStage().draw();
			getStage().setScale(new Vector2d(1, 1));
		}
		flayers.clear();
		currentLayer = -1;
		controllerLayer = null;
		controller = null;
    }

	public boolean updatePrimeProperty(NodeProperty parent, PrimeProperty rp) {
		if(parent == this.getResourceProperty()) {
			return parent.updateView(this, rp);
		}
		for(Flayer flayer : flayers) {
			if(flayer.updatePrimeProperty(parent, rp)) {
				return true;
			}
		}
		return false;
    }

	public Flayer createLayer() {
		if(currentLayer != -1) {
			flayers.get(currentLayer).unselect();
		}
		Flayer flayer = new Flayer(this, null, getContext());
		flayers.add(flayer);
		currentLayer = flayers.size() - 1;
		flayer.getNode().setZIndex(currentLayer);
		flayers.get(currentLayer).select();
		
		createControllerLayer();
		return flayer;
    }
	
	private void createControllerLayer() {
		if(DisplayMode.Edit.equals(mode)) {
			if(controllerLayer != null) {
				getStage().remove(controllerLayer.getNode());
			}
			controllerLayer = new Flayer(this, null, getContext());
			controller = new Controller(controllerLayer, getContext());
			controllerLayer.getNode().draw();
		}
	}

	public void setCurrentLayer(Flayer flayer) {
		if(currentLayer != -1 && flayers.get(currentLayer).hashCode() == flayer.hashCode()) {
			return;
		}
		if(currentLayer != -1) {
			flayers.get(currentLayer).unselect();
		}
		for(int i=0; i<flayers.size(); i++) {
			Flayer fl = flayers.get(i);
			if(fl.hashCode() == flayer.hashCode()) {
				currentLayer = i;
				fl.select();
				break;
			}
		}
    }
	
	public Controller getController() {
		return controller;
	}

	public void drawControllerLayer() {
		controllerLayer.getNode().draw();
    }

	public void hideControllerLayer() {
		controllerLayer.getNode().draw();
    }

	public boolean selectNode(ResourceProperty nodeProperty) {
		for(Flayer flayer : flayers) {
			if(flayer.selectNode(nodeProperty))
				return true;
		}
		return false;
    }

	public void load(FlyerProperty flyerProperty) {
		reset();
		this.nodeProperty = flyerProperty;
		for(ResourceProperty rp : flyerProperty.getChildren()) {
			if(rp instanceof LayerProperty) {
				Flayer flayer = new Flayer(this, null, getContext());
				flayers.add(flayer);
				flayer.load((LayerProperty)rp);
				flayer.getLayer().draw();
				
				Integer zindex = rp.getIntProperty(NodeProperty.NameZIndex);
				if(zindex != null)
					flayer.getNode().setZIndex(zindex);
			}
            else if(rp.getName().equals(FlyerProperty.NameFlyerWidth)) {
                double w = ((DoubleProperty)rp).getValue();
                getNode().setWidth((int) w);
            }
            else if(rp.getName().equals(FlyerProperty.NameId)) {
                String id = ((StringProperty)rp).getValue();
                if(id != null)
                    getNode().setID(id);
            }
			else if(rp.getName().equals(FlyerProperty.NameFlyerHeight)) {
				double h = flyerProperty.getDoubleProperty(FlyerProperty.NameFlyerHeight);
				getNode().setHeight((int) h);
			}
		}
		createControllerLayer();
    }

	@Override
    protected Stage createKineticNode() {
	    return null;
    }

	@Override
    public FlyerProperty createResourceProperty() {
	    FlyerProperty fp = new FlyerProperty();
	    fp.fromFnode(this);
		return fp;
    }

	public void deleteShape(ShapeProperty sp) {
		for(Flayer flayer : flayers) {
			if(flayer.deleteShape(sp)) {
				flayer.getNode().draw();
				drawControllerLayer();
				return;
			}
		}
    }

	public boolean moveDown(LayerProperty layer, NodeProperty node) {
		for(int i=0; i<flayers.size(); i++) {
			Flayer fl = flayers.get(i);
			if(fl.getResourceProperty().equals(layer)) {
				return fl.moveDown(node);
			}
		}
		return false;
    }

	public boolean moveUp(LayerProperty layer, NodeProperty node) {
		for(int i=0; i<flayers.size(); i++) {
			Flayer fl = flayers.get(i);
			if(fl.getResourceProperty().equals(layer)) {
				return fl.moveUp(node);
			}
		}
		return false;
    }

	public DisplayMode getMode() {
	    return mode;
    }

	public void setMode(DisplayMode mode) {
	    this.mode = mode;
    }
	
	public void getImage(Stage.DataUrlTarget callback) {
		getNode().toDataURL(callback);
	}

	public void onImgLoaded() {
		for(Flayer f : flayers) {
			f.getNode().draw();
		}
    }

    public Fnode createNewNodeInCurrentLayer(String class1, NodeProperty np) {
        Fnode fnode = flayers.get(currentLayer).createNewNode(class1, np);
        draw();
        return fnode;
    }

    public void deleteCurrentSelected() {
        if(controller.getTarget() != null) {
            if(Window.confirm("Are you sure to remove current shape?")) {
                Flayer flayer = controller.getTarget().getFlayer();
                flayer.deleteShape((ShapeProperty) controller.getTarget().getResourceProperty());
                flayer.getNode().draw();
            }
        }
    }

    public Vector2d getStageSize() {
        return getNode().getSize();
    }
    
    public FlyerProperty getTree() {
        FlyerProperty fp = new FlyerProperty();
        fp.fromFnode(this);
        for(Flayer flayer : flayers) {
            LayerProperty lp = flayer.getTree();
            fp.getChildren().add(lp);
        }
        return fp;
    }

    public String getId() {
        return getNode().getID();
    }
}
