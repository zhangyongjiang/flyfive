package com.flyerfive.client.controller;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.DoubleProperty;
import com.google.gwt.user.client.ui.IsWidget;

public class DoubleCell extends PropertyCell<DoubleProperty> implements IsWidget {
    
    public DoubleCell(DoubleProperty prop, FlyerContext ctx) {
        super(prop, ctx);
    }

    public double getValue() {
        try {
            return Double.parseDouble(textbox.getText());
        }
        catch (NumberFormatException e) {
            return Double.parseDouble(getOriginalValue());
        }
    }
    
    @Override
    public void updateView() {
        setOriginalValue(property.getStringValue());
        textbox.setText(property.getStringValue());
    }
}
