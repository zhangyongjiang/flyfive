package com.sencha.gxt.desktopapp.client.canvas;

import com.gaoshin.fbobuilder.client.model.CanvasContainer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

public class CanvasViewImpl extends CanvasContainer {
	private ContentPanel panel;
	private HorizontalLayoutContainer container;
	
	public Widget getContainer() {
		if(container == null) {
			container = new HorizontalLayoutContainer();
			container.add(getPanel());
		}
		return container;
	}
	
	public ContentPanel getPanel() {
		if(panel == null) {
			panel = new ContentPanel();
			panel.setBorders(false);
			panel.setHeaderVisible(false);
			super.setContainer(panel.getBody());
		}
		return panel;
	}
}
