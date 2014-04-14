package com.flyerfive.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.edzard.kinetic.Circle;
import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Event;
import net.edzard.kinetic.Group;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Line;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Node.EventListener;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.NodeProperty;
import com.flyerfive.client.message.NodeMoveEndMsg;
import com.flyerfive.client.message.NodeMoveMsg;
import com.flyerfive.client.message.NodeSelectedMsg;

public class Controller implements EventListener{
	private static final Logger logger = Logger.getLogger("Controller");
	
	public static interface EventListener {
		boolean onEvent(Event.Type type, EventListener handler);
	}
	
	public class ControllerAnchor {
		public Circle circle;
		public MarkedPosition mp;

		public ControllerAnchor(MarkedPosition v) {
			this.mp = v;
			circle = Kinetic.createCircle(v.getPos(), getAnchorRadius());
			group.add(circle);
			circle.addEventListener(Event.Type.DRAGMOVE, new Node.EventListener() {
				@Override
				public boolean handle(Event evt) {
					Vector2d newPos = circle.getPosition();
					boolean anchorChanged = target.onAnchorDragMove(ControllerAnchor.this, newPos);
					target.onDragMove();
					if(anchorChanged)
						update();
					target.getFlayer().getLayer().draw();
					return false;
				}
			});
			circle.addEventListener(Event.Type.DRAGEND, new Node.EventListener() {
				@Override
				public boolean handle(Event evt) {
					cxt.getMb().sendMsg(new NodeMoveEndMsg(target));
					return false;
				}
			});
	    }

		public void setZIndex(int i) {
			circle.setZIndex(i);
        }
	}
	
	private FlyerContext cxt;
	private Fnode target;
	private Flayer flayer;
	private Group group;
	private Line hline;
	private Line vline;
	private Line hline2;
	private Line vline2;
	private Circle positionAnchor;
	private Circle rotation;
	private List<ControllerAnchor> anchors = new ArrayList<Controller.ControllerAnchor>();
	
	public Controller(Flayer layer, FlyerContext cxt) {
	    this.cxt = cxt;
		this.flayer = layer;
		group = Kinetic.createGroup();
		group.setDraggable(false);
		createRotationTool();
		flayer.getLayer().add(group);
		rotation.setZIndex(10000);
    }
	
	private void createRotationTool() {
		{
			Vector2d start = new Vector2d(8, 0);
			Vector2d end = new Vector2d(2000, 0);
			hline = Kinetic.createLine(start, end);
		}
		{
			Vector2d start = new Vector2d(0, 8);
			Vector2d end = new Vector2d(0, 2000);
			vline = Kinetic.createLine(start, end);
		}
		{
			Vector2d start = new Vector2d(-8, 0);
			Vector2d end = new Vector2d(-2000, 0);
			hline2 = Kinetic.createLine(start, end);
		}
		{
			Vector2d start = new Vector2d(0, -8);
			Vector2d end = new Vector2d(0, -2000);
			vline2 = Kinetic.createLine(start, end);
		}
		hline.setStroke(Colour.red);
		vline.setStroke(Colour.red);
		hline2.setStroke(Colour.red);
		vline2.setStroke(Colour.red);
		List<Integer> dash = new ArrayList<Integer>();
		dash.add(1); dash.add(1); 
		hline.setDashes(dash);
		vline.setDashes(dash);
		hline2.setDashes(dash);
		vline2.setDashes(dash);
		group.add(hline);
		group.add(vline);
		group.add(hline2);
		group.add(vline2);
		
		Vector2d pos = new Vector2d(100, 0);
		rotation = Kinetic.createCircle(pos, 8);
		rotation.setStroke(Colour.red);
		flayer.getLayer().add(rotation);
		
		pos = new Vector2d(0, 0);
		positionAnchor = Kinetic.createCircle(pos, 8);
		positionAnchor.setStroke(Colour.red);
		flayer.getLayer().add(positionAnchor);
		
		showRotationTool();
		
		rotation.addEventListener(Event.Type.DRAGMOVE, new Node.EventListener() {
			@Override
			public boolean handle(Event evt) {
				Vector2d p0 = target.getNode().getPosition();
				Vector2d newPos = rotation.getPosition();
				
				double deltaY = newPos.y - p0.y;
				double deltaX = newPos.x - p0.x;
				double angleInDegrees = Math.atan2(deltaY, deltaX);
				group.setRotation(angleInDegrees);
				target.setRotation(angleInDegrees);
				updateAnchors();
				target.getFlayer().getLayer().draw();
				cxt.getMb().sendMsg(new NodeMoveMsg(target));
				return false;
			}
		});
		rotation.addEventListener(Event.Type.DRAGEND, new Node.EventListener() {
			@Override
            public boolean handle(Event evt) {
				cxt.getMb().sendMsg(new NodeMoveEndMsg(target));
	            return false;
            }
		});
		rotation.addEventListener(Event.Type.DBLCLICK, new Node.EventListener() {
			@Override
            public boolean handle(Event evt) {
				setTarget(null);
	            return false;
            }
		});
		
		positionAnchor.addEventListener(Event.Type.DRAGMOVE, new Node.EventListener() {
			@Override
			public boolean handle(Event evt) {
				Vector2d position = positionAnchor.getPosition();
				target.getNode().setPosition(position);
				updateAnchors();
				update();
				target.getNode().getLayer().draw();
                target.updateResourceProperty(NodeProperty.NamePositionX, NodeProperty.NamePositionY);
                cxt.getMb().sendMsg(new NodeMoveMsg(target));
				return false;
			}
		});
		positionAnchor.addEventListener(Event.Type.DRAGEND, new Node.EventListener() {
			@Override
            public boolean handle(Event evt) {
				cxt.getMb().sendMsg(new NodeMoveEndMsg(target));
	            return false;
            }
		});
		positionAnchor.addEventListener(Event.Type.DBLCLICK, new Node.EventListener() {
			@Override
            public boolean handle(Event evt) {
				setTarget(null);
	            return false;
            }
		});
    }

	public Fnode getTarget() {
	    return target;
    }
	
	public void setTarget(Fnode newTarget) {
		if(target != null && newTarget != null && target.hashCode() == newTarget.hashCode()) {
			update();
			return;
		}
		
		for(ControllerAnchor ca : anchors) {
			group.remove(ca.circle);
		}
		anchors.clear();
		
	    this.target = newTarget;
		if(newTarget == null) {
			showRotationTool();
		}
		else {
			positionAnchor.setPosition(target.getNode().getPosition());
		    setGroupRotation(target.getNode().getRotation());
		    setGroupPosition(target.getNode().getPosition());
			List<MarkedPosition> anchorPositions = target.getAnchors();
			for(MarkedPosition v : anchorPositions) {
				ControllerAnchor anchor = new ControllerAnchor(v);
				anchors.add(anchor);
			}
		}		
		
		cxt.getMb().sendMsg(new NodeSelectedMsg(target));
    }
	
	private void setGroupPosition(Vector2d newPos) {
	    group.setPosition(newPos);
	    Vector2d pos = new Vector2d(100, 0);
	    Vector2d convert = revert(pos, target.getNode().getRotation());
	    convert.add(newPos);
	    rotation.setPosition(convert);
	    showRotationTool();
	}
	
	private void showRotationTool() {
		if(target == null) {
			hline.hide();
			vline.hide();
			hline2.hide();
			vline2.hide();
		}
		else {
			hline.show();
			vline.show();
			hline2.show();
			vline2.show();
		}
		if(target == null || !target.isRotatable()) {
			rotation.hide();
			positionAnchor.hide();
		}
		else {
			rotation.show();
			positionAnchor.show();
		}
	}

	private void setGroupRotation(double newRotation) {
	    group.setRotation(newRotation);
	}

	private void update() {
		setGroupPosition(target.getNode().getPosition());
		setGroupRotation(target.getNode().getRotation());
		positionAnchor.setPosition(target.getNode().getPosition());
		updateAnchors();
    }

	private void updateAnchors() {
		List<MarkedPosition> anchorPositions = target.getAnchors();
		for(int i=0; i<anchors.size(); i++) {
			ControllerAnchor v = anchors.get(i);
			v.mp = anchorPositions.get(i);
			anchors.get(i).circle.setPosition(v.mp.getPos());
		}
    }

	private double getAnchorRadius() {
		return 5;
	}
	
	public void onScaleChanged() {
    }

	@Override
    public boolean handle(Event evt) {
	    return false;
    }

	public static Vector2d offset(Vector2d v0, Vector2d v1) {
		return new Vector2d(v1.x - v0.x, v1.y - v0.y);
	}

	public static Vector2d revert(Vector2d v0, double a) {
		double x1 = v0.x * Math.cos(a) - v0.y * Math.sin(a);
		double y1 = v0.x * Math.sin(a) + v0.y * Math.cos(a);
		Vector2d v1 = new Vector2d(x1, y1);
		return v1;
	}
	
	public static Vector2d convert(Vector2d v0, double a) {
		double x1 = v0.x * Math.cos(a) + v0.y * Math.sin(a);
		double y1 = -v0.x * Math.sin(a) + v0.y * Math.cos(a);
		Vector2d v1 = new Vector2d(x1, y1);
		return v1;
	}

	public void setZIndex(int i) {
		i += 1000;
		group.setZIndex(i++);
		hline.setZIndex(i++);
		vline.setZIndex(i++);;
		hline2.setZIndex(i++);
		vline2.setZIndex(i++);;
		rotation.setZIndex(i++);
		for(ControllerAnchor c : anchors) {
			c.setZIndex(i++);
		}
    }

	public void hide() {
		group.hide();
		rotation.hide();
    }

	public void show() {
		group.show();
		rotation.show();
    }
}
