package com.flyerfive.client.controller;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.ZindexProperty;
import com.flyerfive.client.message.SelectNextNodeMsg;
import com.flyerfive.client.message.SelectPrevNodeMsg;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ZindexCell extends PropertyCell<ZindexProperty> implements IsWidget {
    
    private ZindexDialog.ChangeListener listener;
    private Label label;

    public ZindexCell(ZindexProperty prop, FlyerContext ctx) {
        super(prop, ctx);
    }

    @Override
    public Widget asWidget() {
        label = new Label(getOriginalValue());
        label.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ZindexDialog dialog = new ZindexDialog();
                dialog.setListener(listener);
                dialog.show();
            }
        });
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");

        HTML prev = new HTML("<div style='float:left'>&lt;&lt;</div>");
        prev.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ctx.getMb().sendMsg(new SelectPrevNodeMsg());
            }
        });
        panel.add(prev);
        
        panel.add(label);
        
        HTML next = new HTML("<div style='float:right'>&gt;&gt;</div>");
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ctx.getMb().sendMsg(new SelectNextNodeMsg());
            }
        });
        panel.add(next);
        
        return panel;
    }

    public ZindexDialog.ChangeListener getListener() {
        return listener;
    }

    public void setListener(ZindexDialog.ChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateView() {
        setOriginalValue(property.getStringValue());
        label.setText(getOriginalValue());
    }
}
