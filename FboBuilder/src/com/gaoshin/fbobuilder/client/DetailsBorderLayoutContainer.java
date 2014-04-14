package com.gaoshin.fbobuilder.client;

import com.gaoshin.fbobuilder.client.data.FlyerProperty;
import com.gaoshin.fbobuilder.client.data.ResourceProperty;
import com.gaoshin.fbobuilder.client.model.DisplayMode;
import com.gaoshin.fbobuilder.client.service.FileManagerCallback;
import com.gaoshin.fbobuilder.client.service.WebService;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sencha.gxt.desktopapp.client.canvas.CanvasViewImpl;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class DetailsBorderLayoutContainer extends SimplePanel {
	private CanvasViewImpl canvasImpl;
	
	public CanvasViewImpl getCanvas() {
		return canvasImpl;
	}
	
	public DetailsBorderLayoutContainer() {

		canvasImpl = new CanvasViewImpl();
		canvasImpl.setDisplayMode(DisplayMode.ViewOnly);
	    
	    String id = Window.Location.getParameter("id");
	    try {
	    	int pos = id.indexOf("/", 1);
	        WebService.getFileForUser(id.substring(1, pos), id.substring(pos), new FileManagerCallback() {
	        	@Override
	        	public void onSuccess(JSONObject result) {
            		JSONObject jsonValue = (JSONObject) result.get("data");
            		try {
                        FlyerProperty fp = (FlyerProperty) ResourceProperty.buildResourcePropertyFromJsonObject(jsonValue);
                        int width = fp.getWidth().intValue();
                        int height = fp.getHeight().intValue();
                        fp.setPermission(((JSONString)result.get("permission")).stringValue());
                        fp.sortByZindex();
                        ContentPanel container = canvasImpl.getPanel();
                        container.setBodyBorder(false);
                        canvasImpl.load(fp);
						DetailsBorderLayoutContainer.this.add(container);
						container.getElement().getStyle().setBorderColor("#eee");
						container.getElement().getStyle().setBorderWidth(1, Unit.PX);
						container.getElement().getStyle().setOverflow(Overflow.HIDDEN);
						
                        Viewport v = new Viewport();
                        CenterLayoutContainer c = new CenterLayoutContainer();
                        c.setBorders(false);
                        c.add(DetailsBorderLayoutContainer.this);
                        v.add(c);
                        RootPanel.get("FlyerFiveContainer").add(v);
                    } catch (Exception e) {
                    	e.printStackTrace();
                    	Window.alert("Error\n" + e.getMessage());
                    }
	        	}
	        	
	        	@Override
	        	public void onError(int code, String msg) {
	            	Window.alert("Error\n" + msg);
	        	}
	        });
        } catch (Exception e) {
        	Window.alert("Error\n" + e.getMessage());
        }
	}

}
