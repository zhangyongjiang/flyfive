package com.flyerfive.client.controller;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.PrimeProperty;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PropertyCell<T extends PrimeProperty> implements IsWidget {
    protected T property;
    private String originalValue;
    protected TextBox textbox;
    protected FlyerContext ctx;
    private ChangeHandler handler;

    public PropertyCell(T prop, FlyerContext ctx) {
        this.ctx = ctx;
        this.property = prop;
        originalValue = prop.getStringValue();
    }

    public T getProp() {
        return property;
    }

    public void setProp(T prop) {
        this.property = prop;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }
    
    public void restoreValue() {
        property.setStringValue(originalValue);
    }

    public void updateView() {
        originalValue = property.getStringValue();
        textbox.setText(originalValue);
    }

    @Override
    public Widget asWidget() {
        textbox = new TextBox();
        textbox.setWidth("190px");
        textbox.setText(getOriginalValue());
        if(handler != null) {
            textbox.addChangeHandler(handler);
        }
        return textbox;
    }

    public ChangeHandler getHandler() {
        return handler;
    }

    public void setHandler(ChangeHandler handler) {
        this.handler = handler;
    }

    public String getStringValue() {
        return textbox.getText();
    }
}
