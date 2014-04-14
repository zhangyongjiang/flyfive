package com.flyerfive.client.message;

import com.flyerfive.client.model.Fnode;

public class NodeMoveEndMsg extends Message {
	private Fnode fnode;

	public NodeMoveEndMsg(Fnode fnode) {
		this.fnode = fnode;
    }
	
	public Fnode getFnode() {
	    return fnode;
    }

	public void setFnode(Fnode fnode) {
	    this.fnode = fnode;
    }
}
