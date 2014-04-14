package com.gaoshin.fbobuilder.client.model;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Image;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Vector2d;

import com.gaoshin.fbobuilder.client.data.ImageProperty;
import com.gaoshin.fbobuilder.client.model.Controller.ControllerAnchor;
import com.gaoshin.fbobuilder.client.service.WebService;
import com.google.gwt.http.client.URL;
import com.sencha.gxt.desktopapp.client.DesktopApp;

public class Fimage extends Fshape<Image, ImageProperty> {
	public Fimage(Flayer flayer, ImageProperty ip) {
	    super(flayer, ip);
    }

	@Override
    public List<MarkedPosition> getAnchors() {
		List<MarkedPosition> list = new ArrayList<MarkedPosition>();
	    Vector2d size = node.getSize();
	    
//	    Vector2d topleft = new Vector2d(0, 0);
//	    list.add(new MarkedPosition(node.getPosition(), "topleft", topleft));
	    
	    Vector2d rightbottom = new Vector2d(size.x, size.y);
	    list.add(new MarkedPosition(node.getPosition(), "rightbottom", rightbottom));
	    
//	    Vector2d topright = new Vector2d(size.x, 0);
//	    list.add(new MarkedPosition(node.getPosition(), "topright", topright));
//	    
//	    Vector2d leftbottom = new Vector2d(0, size.y);
//	    list.add(new MarkedPosition(node.getPosition(), "leftbottom", leftbottom));
	    
	    return list;
    }

	@Override
    protected Image createKineticNode() {
		String url = initNodeProperty.getStrProperty(ImageProperty.NameUrl);
		if(!url.startsWith(WebService.getProxyUrl())) {
			url = WebService.getProxyUrl() + "?url=" + URL.encodeQueryString(url);
		}
		DesktopApp.loadImg(url);
	    Vector2d position = nearTopLeft();
		final com.google.gwt.user.client.ui.Image img = new com.google.gwt.user.client.ui.Image(url);
		Image image = Kinetic.createImage(position , img);
		return image;
    }
	
	public boolean onAnchorDragMove(ControllerAnchor anchor, Vector2d newPos) {
		MarkedPosition pos = anchor.mp;
		if("topleft".equals(pos.getId())) {
			double offsetx = - newPos.x;
			double offsety = - newPos.y;
			Vector2d size = node.getSize();
			size.x += offsetx;
			size.y += offsety;
			node.setSize(size);
			
			Vector2d v = Controller.convert(newPos, -node.getRotation());
			v.add(anchor.mp.getOriginalNodePos());
			node.setPosition(v);
		}
		else if("rightbottom".equals(pos.getId())) {
			double offsetx = newPos.x - pos.getPos().x;
			double offsety = newPos.y - pos.getPos().y;
			Vector2d size = node.getSize();
			size.x += offsetx;
			size.y += offsety;
			pos.getPos().x = newPos.x;
			pos.getPos().y = newPos.y;
			node.setSize(size);
			
			getResourceProperty().updateOrCreateDoubleProperty(ImageProperty.NameSizeX, node.getSize().x);
			getResourceProperty().updateOrCreateDoubleProperty(ImageProperty.NameSizeY, node.getSize().y);
		}
		else if("leftbottom".equals(pos.getId())) {
		}
		else if("topright".equals(pos.getId())) {
		}
		return true;
	}

	@Override
    public ImageProperty createResourceProperty() {
		ImageProperty prop = new ImageProperty();
		prop.fromFnode(this);
		return prop;
    }

	public void load(ImageProperty rp) {
		rp.toFnode(this);
		setNodeProperty(rp);
    }
}
