package com.flyerfive.client.controller;

import net.auroris.ColorPicker.client.ColorListener;
import net.auroris.ColorPicker.client.ColorPickerDialog;
import net.edzard.kinetic.Colour;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.controller.images24.Images24;
import com.flyerfive.client.data.ColorProperty;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class ColorCell extends PropertyCell<ColorProperty> implements IsWidget {
    
    private ColorListener listener;

    public ColorCell(ColorProperty prop, FlyerContext ctx) {
        super(prop, ctx);
    }

    @Override
    public Widget asWidget() {
        Widget widget = super.asWidget();
        widget.setWidth("150px");
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(widget);
        Image img = new Image(Images24.getImageResources().color());
        panel.add(img);
        img.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                final ColorPickerDialog dialog = new ColorPickerDialog();
                dialog.setColorListener(new ColorListener() {
                    
                    @Override
                    public void onOk(String color) {
                        if(listener != null)
                            listener.onOk(color);
                    }
                    
                    @Override
                    public void onColor(String color) {
                        Colour colour = new Colour("#" + color);
                        colour.setAlpha(property.getValue().getAlpha());
                        textbox.setText(colour.toString());
                        if(listener != null)
                            listener.onColor(color);
                    }
                    
                    @Override
                    public void onCancel() {
                        if(listener != null)
                            listener.onCancel();
                        textbox.setText(getOriginalValue());
                    }
                });
                dialog.show();
            }
        });
        
        return panel;
    }

    public ColorListener getListener() {
        return listener;
    }

    public void setListener(ColorListener listener) {
        this.listener = listener;
    }

    public String getValue() {
        return textbox.getText();
    }
}
