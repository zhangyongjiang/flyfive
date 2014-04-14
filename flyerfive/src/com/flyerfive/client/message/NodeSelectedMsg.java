package com.flyerfive.client.message;

import com.flyerfive.client.model.Fnode;

public class NodeSelectedMsg extends Message {
	private Fnode fnode;

	public NodeSelectedMsg(Fnode fnode) {
		this.fnode = fnode;
    }
	
	public Fnode getFnode() {
	    return fnode;
    }

	public void setFnode(Fnode fnode) {
	    this.fnode = fnode;
    }
}
