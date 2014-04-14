package com.flyerfive.client.controller;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ZindexDialog extends DialogBox
{
    public static interface ChangeListener{
        void moveUp();
        void moveDown();
    }

    private ChangeListener listener;
    
    public ZindexDialog()
    {
        setText("Z-index");

        setWidth("100px");
        setHeight("120px");
        setGlassEnabled(true);

        int clientWidth = Window.getClientWidth();
        setPopupPosition(clientWidth - 500, 10);

        // Define the panels
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100px");
        panel.setHeight("120px");

        // Define the buttons
        Button down = new Button("Move Down");
        down.setWidth("100px");
        down.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                if(listener != null)
                    listener.moveDown();
            }
        });

        Button up = new Button("Move Up");
        up.setWidth("100px");
        up.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                if(listener != null)
                    listener.moveUp();
            }
        });

        Button done = new Button("Done");
        done.setWidth("100px");
        done.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ZindexDialog.this.hide();
            }
        });
        panel.add(up);
        panel.add(down);
        panel.add(done);

        // Put it together
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        setWidget(panel);
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

}