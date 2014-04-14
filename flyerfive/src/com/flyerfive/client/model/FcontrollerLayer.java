package com.flyerfive.client.model;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.LayerProperty;

public class FcontrollerLayer extends Flayer {
	private Controller controller;

	public FcontrollerLayer(Fstage fstage, LayerProperty lp, FlyerContext cxt) {
	    super(fstage, lp, cxt);
	    controller = new Controller(this, cxt);
    }

	public Controller getController() {
	    return controller;
    }

	public void draw() {
		node.draw();
    }
}
