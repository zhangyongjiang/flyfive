package com.sencha.gxt.desktopapp.client.canvas.images24;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class Images24 {

	public interface ImageResources extends ClientBundle {
		ImageResource home_24();
		ImageResource copy_24();
		ImageResource paste_24();
		ImageResource text_icon_24();
		ImageResource text_24();
		ImageResource circle_24();
		ImageResource line_24();
		ImageResource folder_24();
		ImageResource ellipse_24();
		ImageResource help_24();
		ImageResource exit_24();
		ImageResource f5_blue_24();
		ImageResource flight_28();
		ImageResource polygon_24();
		ImageResource polygon2_24();
		ImageResource polygon3_24();
		ImageResource remove_24();
		ImageResource zoomin_24();
		ImageResource zoomout_24();
		ImageResource picture_24();
		ImageResource rectangle_24();
		ImageResource music_24();
		ImageResource play_24();
		ImageResource layers_34();
		ImageResource layers_24();
		ImageResource green_circle_plus_24();
		ImageResource video_24();
		ImageResource flight_24();
	}

	private static ImageResources imageResources;

	public static ImageResources getImageResources() {
		if (imageResources == null) {
			imageResources = GWT.create(ImageResources.class);
		}
		return imageResources;
	}
}
