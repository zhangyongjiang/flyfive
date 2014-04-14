package com.gaoshin.fbobuilder.client.model;


public interface CanvasEventListener  {

	void onNodeMove(Fstage fstage, Flayer flayer, Fnode node);

	void onNodeSelected(Fnode fnode);

}
