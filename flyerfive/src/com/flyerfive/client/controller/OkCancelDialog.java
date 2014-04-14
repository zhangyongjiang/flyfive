package com.flyerfive.client.controller;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OkCancelDialog<T extends OkCancelHandler> extends DialogBox {
    private T handler;
    protected VerticalPanel pandel;
    protected FlowPanel content;

    public T getHandler() {
        return handler;
    }

    public void setHandler(T handler) {
        this.handler = handler;
    }
    
    public OkCancelDialog() {
        pandel = new VerticalPanel();
        content = new FlowPanel();
        pandel.add(content);
        
        HorizontalPanel okcancel = new HorizontalPanel();
        
        Button ok = new Button("Ok");
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OkCancelDialog.this.hide();
                if(handler != null)
                    handler.onOk();
            }
        });
        okcancel.add(ok);
        
        Button cancel = new Button("Cancel");
        cancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OkCancelDialog.this.hide();
                if(handler != null)
                    handler.onCancel();
            }
        });
        okcancel.add(cancel);
        
        setWidget(pandel);
    }
}
