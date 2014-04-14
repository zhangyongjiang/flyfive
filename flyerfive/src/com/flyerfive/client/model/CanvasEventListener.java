package com.flyerfive.client.model;


public interface CanvasEventListener  {

	void onNodeMove(Fstage fstage, Flayer flayer, Fnode node);

	void onNodeSelected(Fnode fnode);

}
