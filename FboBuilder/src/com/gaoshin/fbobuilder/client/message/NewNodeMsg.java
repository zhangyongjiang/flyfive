package com.gaoshin.fbobuilder.client.message;


public class NewNodeMsg extends Message {
	private String nodeCls;
	
	public NewNodeMsg(String nodeCls) {
		this.nodeCls = nodeCls;
    }

	public String getNodeCls() {
	    return nodeCls;
    }

	public void setNodeCls(String nodeCls) {
	    this.nodeCls = nodeCls;
    }

}
