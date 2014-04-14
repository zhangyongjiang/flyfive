package com.gaoshin.fbobuilder.client.message;

public class ExportImgMsg extends Message {
	private boolean openNewWindow = true;

	public ExportImgMsg() {
    }
	
	public ExportImgMsg(boolean openNewWindow) {
		this.openNewWindow = openNewWindow;
    }
	
	public boolean isOpenNewWindow() {
	    return openNewWindow;
    }

	public void setOpenNewWindow(boolean openNewWindow) {
	    this.openNewWindow = openNewWindow;
    }
}
