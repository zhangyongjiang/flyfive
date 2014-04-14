package com.gaoshin.fbobuilder.client.message;

import com.gaoshin.fbobuilder.client.model.Fnode;

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
