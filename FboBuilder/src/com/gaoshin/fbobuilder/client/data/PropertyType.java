package com.gaoshin.fbobuilder.client.data;

import com.gaoshin.fbobuilder.client.resourcemanager.image.ResourceTreeImages;
import com.google.gwt.resources.client.ImageResource;

public enum PropertyType {
	Node(ResourceTreeImages.INSTANCE.folder()),
	Shape(ResourceTreeImages.INSTANCE.folder()),
	Line(ResourceTreeImages.INSTANCE.line_16x16()),
	Flyer(ResourceTreeImages.INSTANCE.f5_blue_16()),
	Layer(ResourceTreeImages.INSTANCE.layer_16()), 
	Image(ResourceTreeImages.INSTANCE.butterfly_20x18()), 
	FlyerFolder(null), 
	Audio(ResourceTreeImages.INSTANCE.music()), 
	Prime(ResourceTreeImages.INSTANCE.java()), 
	Stage(ResourceTreeImages.INSTANCE.music()), 
	Circle(ResourceTreeImages.INSTANCE.circle_16x16()), 
	Ellipse(ResourceTreeImages.INSTANCE.ellipse_16x16()), 
	Rectangle(ResourceTreeImages.INSTANCE.rect_16x16()), 
	PathSVG(null), 
	Polygon(ResourceTreeImages.INSTANCE.polygon_16x16()), 
	RegularPolygon(ResourceTreeImages.INSTANCE.regular_polygon_16()), 
	Star(ResourceTreeImages.INSTANCE.star_16x16()),
	Text(ResourceTreeImages.INSTANCE.text_16()),
	Sprite(null),
	TextPath(null),
	File(ResourceTreeImages.INSTANCE.doc_16()),
	;
	
	private ImageResource img;
	private PropertyType(ImageResource img) {
		this.img = img;
	}
	
	public ImageResource getImg() {
		return img;
	}
}
