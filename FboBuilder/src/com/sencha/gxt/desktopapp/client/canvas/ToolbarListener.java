package com.sencha.gxt.desktopapp.client.canvas;

import com.gaoshin.fbobuilder.client.model.Fnode;

public interface ToolbarListener {

	void onZoomout();

	void onZoomin();

	void createNewNode(Class<? extends Fnode> class1);

	void onDelete();

	void onCreateLayer();

	void onCreateFlyer();

	void createFlyerFolder();

}