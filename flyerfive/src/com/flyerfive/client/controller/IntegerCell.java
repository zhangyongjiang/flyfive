package com.flyerfive.client.controller;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.IntProperty;
import com.google.gwt.user.client.ui.IsWidget;

public class IntegerCell extends PropertyCell<IntProperty> implements IsWidget {
    
    public IntegerCell(IntProperty prop, FlyerContext ctx) {
        super(prop, ctx);
    }

    public int getValue() {
        try {
            return Integer.parseInt(textbox.getText());
        }
        catch (NumberFormatException e) {
            return Integer.parseInt(getOriginalValue());
        }
    }
    
    @Override
    public void updateView() {
        setOriginalValue(property.getStringValue());
        textbox.setText(property.getStringValue());
    }
}
