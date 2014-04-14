package com.flyerfive.client.controller;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.controller.images24.Images24;
import com.flyerfive.client.message.ToolbarMsg;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class Toolbar extends FlowPanel {
    public static enum ToolbarType {
        Reset,
        Line,
        Text,
        Image,
        Circle,
        Rect,
        Ellipse,
        RegularPolygon,
        Polygon,
        Video,
        Zoomin,
        Zoomout,
        Remove,
        Copy,
        Paste, 
        Resize, 
        Configure,
        Open,
        Save, 
        DeleteFlyer,
    }
    
    public class ToolbarItem extends Image {
        private ToolbarType type;
        
        public ToolbarItem(String title, ToolbarType type, ImageResource ir) {
            super(ir);
            this.type = type;
            setTitle(title);
            getElement().getStyle().setMarginLeft(8, Unit.PX);
            addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ctx.getMb().sendMsg(new ToolbarMsg(ToolbarItem.this.type));
                }
            });
        }
    }

    private FlyerContext ctx;
    
    public Toolbar(FlyerContext cxt) {
        this.ctx = cxt;
        getElement().getStyle().setMargin(4, Unit.PX);
        setup();
    }
    
    private void setup() {
        setHeight("30px");
        add(new ToolbarItem("Line", ToolbarType.Line, Images24.getImageResources().line()));
        add(new ToolbarItem("Text", ToolbarType.Text, Images24.getImageResources().text()));
        add(new ToolbarItem("Image", ToolbarType.Image, Images24.getImageResources().picture()));
        add(new ToolbarItem("Circle", ToolbarType.Circle, Images24.getImageResources().circle()));
        add(new ToolbarItem("Rectangle", ToolbarType.Rect, Images24.getImageResources().rectangle()));
        add(new ToolbarItem("Ellipse", ToolbarType.Ellipse, Images24.getImageResources().ellipse()));
        add(new ToolbarItem("Polygon", ToolbarType.Polygon, Images24.getImageResources().polygon()));
        add(new ToolbarItem("Regular Polygon", ToolbarType.RegularPolygon, Images24.getImageResources().regular_polygon()));
//        add(new ToolbarItem("Video", ToolbarType.Video, Images24.getImageResources().video()));
        
        ToolbarItem item = new ToolbarItem("Resize", ToolbarType.Resize, Images24.getImageResources().move());
        item.getElement().getStyle().setMarginLeft(24, Unit.PX);
        add(item);
        add(new ToolbarItem("Zoom In", ToolbarType.Zoomin, Images24.getImageResources().zoomin()));
        add(new ToolbarItem("Zoom Out", ToolbarType.Zoomout, Images24.getImageResources().zoomout()));
        
        ToolbarItem reset = new ToolbarItem("Clear", ToolbarType.Reset, Images24.getImageResources().white());
        reset.getElement().getStyle().setMarginLeft(24, Unit.PX);
        add(reset);
        add(new ToolbarItem("Open", ToolbarType.Open, Images24.getImageResources().open()));
        add(new ToolbarItem("Save", ToolbarType.Save, Images24.getImageResources().save()));
        add(new ToolbarItem("Delete Flyer", ToolbarType.DeleteFlyer, Images24.getImageResources().delete()));
        
//        add(new ToolbarItem("Remove", ToolbarType.Remove, Images24.getImageResources().remove()));
//        add(new ToolbarItem("Copy", ToolbarType.Copy, Images24.getImageResources().copy()));
//        add(new ToolbarItem("Paste", ToolbarType.Paste, Images24.getImageResources().paste()));
    }
}
