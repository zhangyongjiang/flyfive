package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Event;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Shape;
import net.edzard.kinetic.Stage;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.NodeProperty;
import com.flyerfive.client.data.PrimeProperty;
import com.flyerfive.client.data.ResourceProperty;
import com.flyerfive.client.message.NodeMoveEndMsg;
import com.flyerfive.client.model.Controller.ControllerAnchor;
import com.google.gwt.user.client.Window;

public abstract class Fnode <T extends Node, P extends NodeProperty> {
	abstract protected T createKineticNode();
	abstract public P createResourceProperty();
	
	protected T node;
	protected Flayer flayer;
	protected P nodeProperty;
	protected P initNodeProperty;
	private String url;
    protected FlyerContext ctx;
	
	public Fnode(Flayer fl, P rp, FlyerContext cxt) {
	    this.ctx = cxt;
		this.initNodeProperty = rp;
		if(this instanceof Fstage) {
		}
		else if(this instanceof Flayer) {
			this.flayer = (Flayer) this;
			this.node = createKineticNode();
		}
		else {
			this.flayer = fl;
			this.node = createKineticNode();
			setupEventListener();
			setDefaultValue();
		}
	}
	
    protected void setDefaultValue() {
    }
    
    public FlyerContext getContext() {
        return ctx;
    }
    
	public P getResourceProperty() {
		if(nodeProperty == null)
			nodeProperty = createResourceProperty();
		return nodeProperty;
	}
	
	public void setNodeProperty(P p) {
		nodeProperty = p;
	}
	
	public void updateResourceProperty(String... keys) {
		((P)getResourceProperty()).updateResourceProperty(this, keys);
	}
	
	public Fnode(Flayer fl, T node) {
		this.flayer = fl;
		this.node = node;
		setupEventListener();
	}
	
	public int getTopZindex() {
		int z = 0;
		for(Node node : flayer.getLayer().getChildren()) {
			if(node.getZIndex() > z)
				z = node.getZIndex();
		}
		return z;
	}
	
	private void setupEventListener() {
		int zindex = getTopZindex() + 1;
		flayer.getLayer().add(node);
		node.setZIndex(zindex);
		node.setID(String.valueOf(this.hashCode()));
		node.setDraggable(DisplayMode.Edit.equals(getFstage().getMode()));
		if(DisplayMode.Edit.equals(getFstage().getMode())) {
			node.addEventListener(Event.Type.DRAGMOVE, new Node.EventListener() {
				@Override
				public boolean handle(Event evt) {
					updateResourceProperty(NodeProperty.NamePositionX, NodeProperty.NamePositionY);
					onDragMove();
					return true;
				}
			});
			node.addEventListener(Event.Type.DRAGEND, new Node.EventListener() {
				@Override
				public boolean handle(Event evt) {
					ctx.getMb().sendMsg(new NodeMoveEndMsg(Fnode.this));
					return true;
				}
			});
			node.addEventListener(Event.Type.CLICK, new Node.EventListener() {
				@Override
				public boolean handle(Event evt) {
					flayer.getFstage().setCurrentLayer(flayer);
					toggleSelection();
					return true;
				}
			});
		}
		else {
			if(node instanceof Shape) {
				setupDisplayModeMouseEvent();
			}
		}
	}
	
	protected void setupDisplayModeMouseEvent() {
    }
	
	public void openUrl() {
		Window.open(getUrl(), "_blank", null);
	}
	
	public boolean isRotatable() {
		return true;
	}
	
	protected void onDragMove() {
		flayer.onDragMove(this);
	}
	
	protected void toggleSelection() {
		flayer.toggleSelection(this);
	}
	
	protected boolean getDraggable() {
		return false;
	}
	
	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		return false;
	}
	
	public T getNode() {
		return node;
	}
	
	public Flayer getFlayer() {
		return flayer;
	}
	
	public Fstage getFstage() {
		return flayer.getFstage();
	}
	
	public Vector2d getPosition() {
		return node.getPosition();
	}
	
    public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
	    Box2d bounds = node.getDragBounds();
	    Vector2d topleft = new Vector2d(bounds.left, bounds.top);
	    Vector2d rightbottom = new Vector2d(bounds.left, bounds.top);
	    list.add(new MarkedPosition(node.getPosition(), "topleft", topleft));
	    list.add(new MarkedPosition(node.getPosition(), "rightbottom", rightbottom));
	    return list;
    }
    
    public Vector2d getStageSize() {
    	return flayer.getFstage().getStage().getSize();
    }
    
    public Vector2d getScale() {
    	return flayer.getFstage().getStage().getScale();
    }
    
    public Stage getStage() {
    	return flayer.getFstage().getStage();
    }
    
    public Vector2d nearTopLeft() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x / 4;
    	pos.y = size.y / 4;
    	return pos;
    }
    
    public Vector2d nearBottomLeft() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x / 4;
    	pos.y = size.y * 3 / 4;
    	return pos;
    }
    
    public Vector2d nearBottomRight() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x * 3 / 4;
    	pos.y = size.y * 3 / 4;
    	return pos;
    }
    
    public Vector2d nearTopRight() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x * 3 / 4;
    	pos.y = size.y / 4;
    	return pos;
    }
    
    public Vector2d getCenterPosition() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x / 2;
    	pos.y = size.y / 2;
    	return pos;
    }
    
    public Vector2d newTopCenter() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x / 2;
    	pos.y = size.y / 4;
    	return pos;
    }
    
    public Vector2d newBottomCenter() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x / 2;
    	pos.y = size.y * 3 / 4;
    	return pos;
    }
    
    public Vector2d topCenter() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x / 2;
    	pos.y = 0;
    	return pos;
    }
    
    public Vector2d bottomCenter() {
    	Vector2d pos = new Vector2d();
    	Vector2d size = getStageSize();
    	pos.x = size.x / 2;
    	pos.y = size.y ;
    	return pos;
    }
    
	public void setRotation(double rotation) {
		node.setRotation(rotation);
		getResourceProperty().updateOrCreateDoubleProperty(NodeProperty.NameRotation, rotation);
    }
	
	public boolean updatePrimeProperty(NodeProperty parent, PrimeProperty rp) {
		P property = getResourceProperty();
		for(ResourceProperty child : property.getChildren()) {
			if(child.hashCode() == rp.hashCode()) {
				parent.updateView(this, rp);
				return true;
			}
		}
	    return false;
    }
	public String getUrl() {
	    return url;
    }
	public void setUrl(String url) {
		if(url != null && url.trim().length() > 0) {
			this.url = url;
		}
		else {
			this.url = null;
		}
		setupDisplayModeMouseEvent();
    }
}
