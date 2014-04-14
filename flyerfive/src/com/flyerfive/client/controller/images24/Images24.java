package com.flyerfive.client.controller.images24;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class Images24 {

	public interface ImageResources extends ClientBundle {
        ImageResource white();
        ImageResource home();
		ImageResource copy();
		ImageResource paste();
		ImageResource text_icon();
        ImageResource text();
        ImageResource delete();
		ImageResource circle();
		ImageResource line();
		ImageResource folder();
		ImageResource ellipse();
		ImageResource help();
		ImageResource exit();
		ImageResource f5_blue();
		ImageResource polygon();
		ImageResource regular_polygon();
		ImageResource remove();
		ImageResource zoomin();
		ImageResource zoomout();
		ImageResource picture();
		ImageResource rectangle();
		ImageResource music();
		ImageResource play();
		ImageResource layers();
		ImageResource green_circle_plus();
		ImageResource video();
        ImageResource flight();
        ImageResource move();
        ImageResource edit();
        ImageResource open();
        ImageResource save();
        ImageResource color();
        ImageResource color_palette();
	}

	private static ImageResources imageResources;

	public static ImageResources getImageResources() {
		if (imageResources == null) {
			imageResources = GWT.create(ImageResources.class);
		}
		return imageResources;
	}
}
