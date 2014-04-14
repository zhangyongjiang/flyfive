package com.flyerfive.client.controller;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.WebService;
import com.flyerfive.client.data.FlyerProperty;
import com.flyerfive.client.data.ResourceProperty;
import com.flyerfive.client.model.DisplayMode;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;

public class FlyerFiveWidget extends AbsolutePanel {
    private CanvasContainer canvas;
    private Toolbar toolbar;
    private PropertyEditor editor;
    private FlyerContext ctx;

    public FlyerFiveWidget(FlyerContext cxt) {
        this.ctx = cxt;
        setup();
    }

    private void setup() {
        String flyerId = Window.Location.getParameter("id");
        setupHomeIcon();
        if(flyerId == null)
            setupToolbar();
        setupCanvas();
        if(flyerId == null)
            setupEditor();
    }

    private void setupEditor() {
        PropertyEditor editor = new PropertyEditor(ctx);
//        editor.show();
    }

    private void setupCanvas() {
        canvas = new CanvasContainer(ctx);
        final String flyerId = Window.Location.getParameter("id");
        if(flyerId != null) {
            canvas.setDisplayMode(DisplayMode.ViewOnly);
            try {
                WebService.getFlyer(flyerId, new RequestCallback() {
                    @Override
                    public void onResponseReceived(Request request, Response response) {
                        String json = response.getText();
                        try {
                            FlyerProperty fp = (FlyerProperty) ResourceProperty.buildResourcePropertyFromJsonString(json);
                            if(fp.getId() == null) {
                                fp.setId(flyerId);
                            }
                            canvas.load(fp);
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
        if(flyerId == null)
            add(canvas, 0, 32);
        else
            add(canvas, 0, 0);
    }

    private void setupToolbar() {
        toolbar = new Toolbar(ctx);
        add(toolbar, 0, 0);
    }

    private void setupPropertyEditor() {
        editor = new PropertyEditor(ctx);
        add(editor, 32, 0);
    }

    private void setupHomeIcon() {
        Image f5img = new Image();
        add(f5img, 0, 0);
    }

    public DisplayMode getDisplayMode() {
        return canvas.getDisplayMode();
    }

    public void setMode(DisplayMode mode) {
        canvas.setDisplayMode(mode);
    }

}
