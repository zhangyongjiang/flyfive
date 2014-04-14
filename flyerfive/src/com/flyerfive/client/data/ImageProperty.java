package com.flyerfive.client.data;

import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.model.Fimage;
import com.google.gwt.user.client.ui.Image;

public class ImageProperty extends ShapeProperty<Fimage> {
	public static final String NameSizeX = "image.size.x";
	public static final String NameSizeY = "image.size.y";
	public static final String NameUrl = "image.url";
	
	public ImageProperty() {
		setType(PropertyType.Image);
    }
	
	@Override
	public void fromFnode(Fimage fshape) {
	    super.fromFnode(fshape);
	    Image image = fshape.getNode().getImage();
	    String url = image.getUrl();
	    updateOrCreateStrProperty(NameUrl, url);
	    
	    Vector2d size = fshape.getNode().getSize();
	    updateOrCreateDoubleProperty(NameSizeX, size.x);
	    updateOrCreateDoubleProperty(NameSizeY, size.y);
	}
	
	@Override
	public void toFnode(Fimage fnode) {
	    super.toFnode(fnode);
	    Image image = fnode.getNode().getImage();
		String url = getStrProperty(NameUrl);
		com.google.gwt.user.client.ui.Image.prefetch(url);
		image.setUrl(url);
		
		fnode.getNode().setSize(new Vector2d(getDoubleProperty(NameSizeX), getDoubleProperty(NameSizeY)));
	}
	
	@Override
	public boolean updateView(Fimage fnode, PrimeProperty rp) {
	    boolean found = super.updateView(fnode, rp);
		if(found) return found;
		
		net.edzard.kinetic.Image shape = fnode.getNode();
		if(rp.getName().equals(NameUrl)) {
			String url = getStrProperty(NameUrl);
			com.google.gwt.user.client.ui.Image.prefetch(url);
			shape.getImage().setUrl(url );
			found = true;
		}
		else if(rp.getName().equals(NameSizeX)) {
			Vector2d size = fnode.getNode().getSize();
			size.x = getDoubleProperty(NameSizeX);
			fnode.getNode().setSize(size);
			found = true;
		}
		else if(rp.getName().equals(NameSizeY)) {
			Vector2d size = fnode.getNode().getSize();
			size.y = getDoubleProperty(NameSizeY);
			fnode.getNode().setSize(size);
			found = true;
		}
		
		return found;
	}
}
