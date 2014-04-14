package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.CircleProperty;
import com.flyerfive.client.data.EllipseProperty;
import com.flyerfive.client.data.ImageProperty;
import com.flyerfive.client.data.LayerProperty;
import com.flyerfive.client.data.LineProperty;
import com.flyerfive.client.data.NodeProperty;
import com.flyerfive.client.data.PolygonProperty;
import com.flyerfive.client.data.PrimeProperty;
import com.flyerfive.client.data.RectangleProperty;
import com.flyerfive.client.data.RegularPolygonProperty;
import com.flyerfive.client.data.ResourceProperty;
import com.flyerfive.client.data.ShapeProperty;
import com.flyerfive.client.data.TextProperty;
import com.flyerfive.client.message.NodeMoveMsg;
import com.flyerfive.client.message.NodeSelectedMsg;

public class Flayer extends Fcontainer<Layer, LayerProperty>{
	private static final Logger logger = Logger.getLogger("Flayer");
	
	private List<Fnode> nodes = new ArrayList<Fnode>();
	private Fnode currentNode;
	private String name;
	
	private Fstage fstage;

	public Flayer(Fstage fstage, LayerProperty lp, FlyerContext cxt) {
		super(null, lp, cxt);
		this.fstage = fstage;
		fstage.getStage().add(node);
    }

	@Override
    protected Layer createKineticNode() {
		Layer layer = Kinetic.createLayer();
		return layer;
    }
	
	private Controller getController() {
		return fstage.getController();
	}

	@Override
    public LayerProperty createResourceProperty() {
		LayerProperty prop = new LayerProperty();
		prop.fromFnode(this);
		return prop;
    }
	
	public Layer getLayer() {
		return flayer.getNode();
	}
	
	public List<Fnode> getNodes() {
	    return nodes;
    }

	public Fstage getFstage() {
		return fstage;
	}

	public Fnode getCurrentNode() {
	    return currentNode;
    }

	public void setCurrentNode(Fnode newNode) {
	    this.currentNode = newNode;
    }

	public void unselect() {
		currentNode = null;
    }

	public void select() {
    }

	public Fnode createNewNode(String className, NodeProperty np) {
		try {
			Fnode fnode = null;
			if(Fline.class.getName().equals(className)) {
				fnode = new Fline(this, (LineProperty) np, getContext());
			}
			else if(Frectangle.class.getName().equals(className)) {
				Frectangle rect = new Frectangle(this, (RectangleProperty) np, getContext());
				fnode = rect;
			}
			else if(Fellipse.class.getName().equals(className))
				fnode = new Fellipse(this, (EllipseProperty) np, getContext());
			else if(Fimage.class.getName().equals(className)) {
				fnode = new Fimage(this, (ImageProperty) np, getContext());
			}
			else if(Fcircle.class.getName().equals(className)) {
				fnode = new Fcircle(this, (CircleProperty) np, getContext());
			}
			else if(Ftext.class.getName().equals(className)) {
				fnode = new Ftext(this, (TextProperty) np, getContext());
			}
			else if(Fpolygon.class.getName().equals(className))
				fnode = new Fpolygon(this, (PolygonProperty) np, getContext());
			else if(FregularPolygon.class.getName().equals(className)) {
				fnode = new FregularPolygon(this, (RegularPolygonProperty) np, getContext());
			}
			else 
				throw new RuntimeException(className + " creation is not supported");
			if(fnode != null) {
				nodes.add(fnode);
		        currentNode = fnode;
		        getController().setTarget(fnode);
			}
			
			return fnode;
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
    }

	public void onDragMove(Fnode node) {
		currentNode = node;
		getController().setTarget(node);
		fstage.drawControllerLayer();
		getLayer().draw();
		getContext().getMb().sendMsg(new NodeMoveMsg(node));
	}

	public void toggleSelection(Fnode node) {
		if(currentNode != null && currentNode.hashCode() == node.hashCode()) {
			getController().setTarget(null);
			currentNode = null;
			getLayer().draw();
			fstage.drawControllerLayer();
		}
		else {
			currentNode = node;
			getController().setTarget(node);
			getLayer().draw();
			fstage.drawControllerLayer();
			getContext().getMb().sendMsg(new NodeSelectedMsg(node));
		}
	}

	public String getName() {
	    return name;
    }

	public void setName(String name) {
	    this.name = name;
    }

	public boolean updatePrimeProperty(NodeProperty parent, PrimeProperty rp) {
		for(Fnode fn : nodes) {
			if(fn.updatePrimeProperty(parent, rp)) {
				getController().setTarget(fn);
				getLayer().draw();
				return true;
			}
		}
	    return false;
    }

	public boolean selectNode(ResourceProperty nodeProperty) {
		if(getResourceProperty().equals(nodeProperty))
			return true;
		for(Fnode fn : nodes) {
			if(fn.getResourceProperty().equals(nodeProperty)) {
				currentNode = fn;
				getController().setTarget(fn);
				fstage.drawControllerLayer();
				return true;
			}
		}
	    return false;
    }

	public void load(LayerProperty lp) {
		setNodeProperty(lp);
		name = lp.getStrProperty("name");
		for(ResourceProperty rp : lp.getChildren()) {
			Fnode added = null;
			if(rp instanceof CircleProperty) {
				Fcircle fc = new Fcircle(this, (CircleProperty) rp, getContext());
				fc.load((CircleProperty) rp);
				nodes.add(fc);
				added = fc;
			}
			else if(rp instanceof RectangleProperty) {
				Frectangle fnode = new Frectangle(this, (RectangleProperty) rp, getContext());
				fnode.load((RectangleProperty) rp);
				nodes.add(fnode);
				added = fnode;
			}
			else if(rp instanceof EllipseProperty) {
				Fellipse fnode = new Fellipse(this, (EllipseProperty) rp, getContext());
				fnode.load((EllipseProperty) rp);
				nodes.add(fnode);
				added = fnode;
			}
			else if(rp instanceof ImageProperty) {
				Fimage fnode = new Fimage(this, (ImageProperty) rp, getContext());
				fnode.load((ImageProperty) rp);
				nodes.add(fnode);
				added = fnode;
			}
			else if(rp instanceof TextProperty) {
				Ftext fnode = new Ftext(this, (TextProperty) rp, getContext());
				fnode.load((TextProperty) rp);
				nodes.add(fnode);
				added = fnode;
			}
			else if(rp instanceof LineProperty) {
				Fline fnode = new Fline(this, (LineProperty) rp, getContext());
				fnode.load((LineProperty) rp);
				nodes.add(fnode);
				added = fnode;
			}
			else if(rp instanceof RegularPolygonProperty) {
				FregularPolygon fnode = new FregularPolygon(this, (RegularPolygonProperty) rp, getContext());
				fnode.load((RegularPolygonProperty) rp);
				nodes.add(fnode);
				added = fnode;
			}
			else if(rp instanceof PolygonProperty) {
				Fpolygon fnode = new Fpolygon(this, (PolygonProperty) rp, getContext());
				fnode.load((PolygonProperty) rp);
				nodes.add(fnode);
				added = fnode;
			}
			
			if(added != null) {
				Integer zindex = rp.getIntProperty(NodeProperty.NameZIndex);
				if(zindex != null)
					nodes.get(nodes.size()-1).getNode().setZIndex(zindex);
			}
		}
    }

	public boolean deleteShape(ShapeProperty sp) {
		for(Fnode fn : nodes) {
			if(fn.getResourceProperty().equals(sp)) {
				nodes.remove(fn);
				node.remove(fn.getNode());
				if(fn.equals(currentNode)) {
					currentNode = null;
					getController().setTarget(null);
					getFstage().drawControllerLayer();
				}
				return true;
			}
		}
	    return false;
    }

	public boolean moveUp(NodeProperty prop) {
		for(int i=0; i<nodes.size()-1; i++) {
			Fnode fn = nodes.get(i);
			if(fn.getResourceProperty().equals(prop)) {
				fn.getNode().setZIndex(i + 1);
				prop.updateOrCreateZindexProperty(i+1);
				
				nodes.get(i+1).getNode().setZIndex(i);
				nodes.get(i+1).getResourceProperty().updateOrCreateZindexProperty(i);
				
				nodes.set(i, nodes.get(i+1));
				nodes.set(i+1, fn);
				
				getLayer().draw();
				return true;
			}
		}
		return false;
    }

	public boolean moveDown(NodeProperty prop) {
		for(int i=1; i<nodes.size(); i++) {
			Fnode fn = nodes.get(i);
			if(fn.getResourceProperty().equals(prop)) {
				fn.getNode().setZIndex(i - 1);
				prop.updateOrCreateZindexProperty(i-1);
				
				nodes.get(i-1).getNode().setZIndex(i);
				nodes.get(i-1).getResourceProperty().updateOrCreateZindexProperty(i);
				
				nodes.set(i, nodes.get(i-1));
				nodes.set(i-1, fn);
				
				getLayer().draw();
				return true;
			}
		}
		return false;
    }

    public void selectNextNode(Fnode node) {
        for(int i=0; i<nodes.size(); i++) {
            if(nodes.get(i).getNode().getZIndex() == node.getNode().getZIndex()) {
                i++;
                if(i == nodes.size())
                    i = 0;
                selectNode(nodes.get(i).getResourceProperty());
                break;
            }
        }
    }

    public void selectPrevNode(Fnode node) {
        for(int i=0; i<nodes.size(); i++) {
            if(nodes.get(i).getNode().getZIndex() == node.getNode().getZIndex()) {
                i--;
                if( i < 0)
                    i = nodes.size() - 1;
                selectNode(nodes.get(i).getResourceProperty());
                break;
            }
        }
    }

    public LayerProperty getTree() {
        LayerProperty fp = new LayerProperty();
        fp.fromFnode(this);
        for(Fnode fnode : nodes) {
            ResourceProperty lp = fnode.getResourceProperty();
            fp.getChildren().add(lp);
        }
        return fp;
    }

}
