package com.flyerfive.client.controller;

import com.flyerfive.client.WebService;
import com.flyerfive.client.data.FlyerProperty;
import com.flyerfive.client.data.ResourceProperty;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OpenFlyerDialog extends DialogBox {
    public static interface FileOpenHandler {
        void openFlyer(FlyerProperty fp);
    }
    
    private ListBox list;
    private FileOpenHandler handler;

    public OpenFlyerDialog(FileOpenHandler handler) {
        this.handler = handler;
        
        setText("Open Files");

        setWidth("220px");
        setHeight("160px");
        setGlassEnabled(true);

        int clientWidth = Window.getClientWidth();
        setPopupPosition(clientWidth - 500, 10);

        // Define the panels
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("220px");
        panel.setHeight("160px");
        
        list = new ListBox(true);
        list.setWidth("220px");
        list.setHeight("130px");
        panel.add(list);
        try {
            WebService.listMyFlyers(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    String[] ids = response.getText().split("[\n\r]+");
                    if(ids.length == 0 || ids[0].length() == 0) {
                        Window.alert("No file found");
                        OpenFlyerDialog.this.hide();
                    }
                    else {
                        for(String id : ids) {
                            list.addItem(id);
                        }
                    }
                }
                
                @Override
                public void onError(Request request, Throwable exception) {
                    exception.printStackTrace();
                    Window.alert("Error\n" + exception.getMessage());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            Window.alert("Error\n" + e.getMessage());
        }
        
        list.addDoubleClickHandler(new DoubleClickHandler() {
            
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                openFile();
            }
        });

        // Define the buttons
        Button cancel = new Button("Cancel");
        cancel.setWidth("100px");
        cancel.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                OpenFlyerDialog.this.hide();
            }
        });

        Button ok = new Button("OK");
        ok.setWidth("100px");
        ok.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                openFile();
            }
        });

        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("220px");
        panel.add(hp);
        hp.add(ok);
        hp.add(cancel);

        // Put it together
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        setWidget(panel);
    }

    protected void openFile() {
        OpenFlyerDialog.this.hide();
        final String file = list.getItemText(list.getSelectedIndex());
        try {
            WebService.getFlyer(file, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    String json = response.getText();
                    try {
                        FlyerProperty fp = (FlyerProperty) ResourceProperty.buildResourcePropertyFromJsonString(json);
                        if(OpenFlyerDialog.this.handler != null) {
                            fp.setId(file);
                            OpenFlyerDialog.this.handler.openFlyer(fp);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Window.alert("Error\n" + e.getMessage());
                    }
                }
                
                @Override
                public void onError(Request request, Throwable exception) {
                    exception.printStackTrace();
                    Window.alert("Error\n" + exception.getMessage());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            Window.alert("Error\n" + e.getMessage());
        }
    }
}
