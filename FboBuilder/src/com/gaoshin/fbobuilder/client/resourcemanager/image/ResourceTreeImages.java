package com.gaoshin.fbobuilder.client.resourcemanager.image;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ResourceTreeImages extends ClientBundle {
	public ResourceTreeImages INSTANCE = GWT.create(ResourceTreeImages.class);

	ImageResource folder();
	ImageResource folder_closed_16();
	ImageResource music();
	ImageResource album();
	ImageResource text();
	ImageResource text_16();
	ImageResource plane_16();
	ImageResource layer_16();
	ImageResource f5_blue_16();
	ImageResource plane_32();
	ImageResource java();
	ImageResource json();
	ImageResource circle_16x16();
	ImageResource regular_polygon_16();
	ImageResource ellipse_16x16();
	ImageResource rect_16x16();
	ImageResource star_16x16();
	ImageResource line_16x16();
	ImageResource butterfly_20x18();
	ImageResource polygon_16x16();
	ImageResource three_dots_32();
	ImageResource rightarrow2_16();
	ImageResource DBGreenArrowRight_16();
	ImageResource file_16();
	ImageResource doc_16();
	ImageResource color_16();
	ImageResource audio_16();
	ImageResource flyer_16();
	ImageResource f5_16();
	
	public static HashMap<String, ImageResource> ImgType = new HashMap<String, ImageResource>(){{
		put("png", INSTANCE.color_16());
		put("jpg", INSTANCE.color_16());
		put("gif", INSTANCE.color_16());
		put("mp3", INSTANCE.audio_16());
	}};
}
