package com.gaoshin.fbobuilder.client.model;

import com.gaoshin.fbobuilder.client.data.LayerProperty;

public class FcontrollerLayer extends Flayer {
	private Controller controller;

	public FcontrollerLayer(Fstage fstage, LayerProperty lp) {
	    super(fstage, lp);
//	    node.setZIndex(100000);
	    controller = new Controller(this);
    }

	public Controller getController() {
	    return controller;
    }

	public void draw() {
		node.draw();
    }
}
