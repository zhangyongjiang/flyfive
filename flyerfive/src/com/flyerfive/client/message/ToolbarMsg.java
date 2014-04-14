package com.flyerfive.client.message;

import com.flyerfive.client.controller.Toolbar.ToolbarType;

public class ToolbarMsg extends Message {
    private ToolbarType type;
    
    public ToolbarMsg(ToolbarType type) {
        this.type = type;
    }
    
    public ToolbarType getType() {
        return type;
    }
}
