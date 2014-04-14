package com.flyerfive.client.controller;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Stage;
import net.edzard.kinetic.Vector2d;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.WebService;
import com.flyerfive.client.data.DoubleProperty;
import com.flyerfive.client.data.FlyerProperty;
import com.flyerfive.client.data.ImageProperty;
import com.flyerfive.client.data.LayerProperty;
import com.flyerfive.client.data.NodeProperty;
import com.flyerfive.client.data.PolygonProperty;
import com.flyerfive.client.data.PrimeProperty;
import com.flyerfive.client.data.ResourceProperty;
import com.flyerfive.client.data.ShapeProperty;
import com.flyerfive.client.data.TextProperty;
import com.flyerfive.client.message.MsgHandler;
import com.flyerfive.client.message.ToolbarMsg;
import com.flyerfive.client.model.DisplayMode;
import com.flyerfive.client.model.Fcircle;
import com.flyerfive.client.model.Fellipse;
import com.flyerfive.client.model.Fimage;
import com.flyerfive.client.model.Flayer;
import com.flyerfive.client.model.Fline;
import com.flyerfive.client.model.Fnode;
import com.flyerfive.client.model.Fpolygon;
import com.flyerfive.client.model.Frectangle;
import com.flyerfive.client.model.FregularPolygon;
import com.flyerfive.client.model.Fstage;
import com.flyerfive.client.model.Ftext;
import com.flyerfive.client.model.StageDataCallback;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;

public class CanvasContainer extends SimplePanel {
	private Fstage fstage;
	private DisplayMode mode = DisplayMode.Edit;
    private FlyerContext ctx;
	
	public CanvasContainer(FlyerContext cxt) {
	    this.ctx = cxt;
        getElement().getStyle().setBorderStyle(BorderStyle.DASHED);
        getElement().getStyle().setBorderColor("#ccc");
        getElement().getStyle().setBorderWidth(1, Unit.PX);
        getElement().getStyle().setMargin(4, Unit.PX);
	    setupMsgHandlers();
    }

	private void setupMsgHandlers() {
	    setupToolbarMsgHandler();
    }

    private void setupToolbarMsgHandler() {
        ctx.getMb().registerHandler(ToolbarMsg.class, new MsgHandler<ToolbarMsg>() {
            @Override
            public void processMsg(ToolbarMsg msg) {
                switch (msg.getType()) {
                case Reset:
                    if(Window.confirm("Are you sure?")) {
                        reset();
                        fstage.createLayer();
                    }
                    break;

                case Line:
                    createNewNodeInCurrentLayer(Fline.class.getName(), null);
                    break;
                    
                case Circle:
                    createNewNodeInCurrentLayer(Fcircle.class.getName(), null);
                    break;
                    
                case Rect:
                    createNewNodeInCurrentLayer(Frectangle.class.getName(), null);
                    break;
                    
                case Text:
                    String text = Window.prompt("text", "flyer five. no flash");
                    if(text != null) {
                        TextProperty tp = new TextProperty();
                        tp.setText(text);
                        createNewNodeInCurrentLayer(Ftext.class.getName(), tp);
                    }
                    break;
                    
                case RegularPolygon:
                    createNewNodeInCurrentLayer(FregularPolygon.class.getName(), null);
                    break;
                    
                case Ellipse:
                    createNewNodeInCurrentLayer(Fellipse.class.getName(), null);
                    break;
                    
                case Image:
                    ImageProperty ip = new ImageProperty();
                    String url = Window.prompt("Please give the URL for the image",
                                    "curl-flyer-5-128.png");
                    if (url != null && url.length() > 0) {
                        ip.addStrProperty(ImageProperty.NameUrl, url);
                        createNewNodeInCurrentLayer(Fimage.class.getName(), ip);
                    }
                    break;
                    
                case Polygon:
                    String sides = Window.prompt("sides?", "5");
                    try {
                        int num = Integer.parseInt(sides);
                        PolygonProperty pp = new PolygonProperty();
                        pp.addIntProperty(PolygonProperty.NameSides, num);
                        createNewNodeInCurrentLayer(Fpolygon.class.getName(), pp);
                    }
                    catch (Exception e) {
                        Window.alert("must give a number");
                    }
                    break;
                    
                case Zoomin:
                    zoomIn(1.2f);
                    break;
                    
                case Zoomout:
                    zoomOut(1.2f);
                    break;
                    
                case Remove:
                    fstage.deleteCurrentSelected();
                    break;
                    
                case Resize:
                    Vector2d stageSize = fstage.getStageSize();
                    String size = Window.prompt(
                            "Please give new size with format width,height:", stageSize.x + "," + stageSize.y);
                    if (size == null)
                        return;
                    String[] wh = size.trim().split("[, x;]+");
                    try {
                        setSize(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
                    }
                    catch (NumberFormatException e) {
                        Window.alert("Error\n" + e.getMessage());
                    }
                    break;
                    
                case Configure:
                    break;
                    
                case Save:
                    save();
                    break;
                    
                case Open:
                    open();
                    break;
                    
                case DeleteFlyer:
                    deleteFlyer();
                    break;
                    
                default:
                    break;
                }
            }
        });
    }
    
    protected void deleteFlyer() {
        if(!Window.confirm("are you sure?"))
            return;
        final String id = fstage.getId();
        if(id != null)
            WebService.removeFlyer(id);
        reset();
    }

    protected void open() {
        OpenFlyerDialog dialog = new OpenFlyerDialog(new OpenFlyerDialog.FileOpenHandler() {
            @Override
            public void openFlyer(FlyerProperty fp) {
                load(fp);
            }
        });
        dialog.show();
    }

    private void save() {
        fstage.getController().setTarget(null);
        fstage.drawControllerLayer();
        FlyerProperty flyerProperty = fstage.getTree();
        final String id = flyerProperty.getId();
        final String flyerContent = flyerProperty.toJsonString();
        getImage(new StageDataCallback() {
            @Override
            public void receive(String img) {
                int pos = img.indexOf(",");
                img = img.substring(pos + 1);
                try {
                    WebService.saveFlyer(id, flyerContent, img, new RequestCallback() {
                        @Override
                        public void onResponseReceived(Request request, Response response) {
                            if(response.getStatusCode() == 200) {
                                String id = response.getText();
                                Window.alert("saved. " + id);
                                if(id != null && id.length() > 0) {
                                    fstage.getNode().setID(id);
                                }
                                fstage.getResourceProperty().updateResourceProperty(fstage, NodeProperty.NameId);
                            }
                            else {
                                Window.alert("Error\n" + response.getText());
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
        });
    }

    public void createStage(int width, int height) {
		getElement().getStyle().setWidth(width, Unit.PX);
		getElement().getStyle().setHeight(height, Unit.PX);
		if(fstage == null) {
			Stage stage = Kinetic.createStage(getElement(), width, height);
			this.fstage = new Fstage(stage, null, ctx);
			fstage.setMode(mode);
			fstage.draw();
		}
		else {
			reset();
			fstage.setNodeProperty(null);
			FlyerProperty fp = fstage.getResourceProperty();
			fp.updateOrCreateDoubleProperty(FlyerProperty.NameFlyerWidth, (double)width);
			fp.updateOrCreateDoubleProperty(FlyerProperty.NameFlyerHeight, (double)height);
			fstage.getNode().setWidth(width);
			fstage.getNode().setHeight(height);
	        getElement().getStyle().setWidth(width, Unit.PX);
	        getElement().getStyle().setHeight(height, Unit.PX);
		}
	}

	public void loadJson(String json) throws Exception {
		FlyerProperty data = (FlyerProperty) ResourceProperty.buildResourcePropertyFromJsonString(json);
		load(data);
	}

	public void load(FlyerProperty data) {
		if(fstage == null) {
			Stage stage = Kinetic.createStage(getElement(), data.getWidth().intValue(), data.getHeight().intValue());
			this.fstage = new Fstage(stage, null, ctx);
			fstage.setMode(mode);
		}
		fstage.load(data);
		if(data.getId() != null)
		    fstage.getNode().setID(data.getId());
		getElement().getStyle().setWidth(data.getIntWidth(), Unit.PX);
		getElement().getStyle().setHeight(data.getIntHeight(), Unit.PX);
    }

	public FlyerProperty getData() {
		if(fstage == null)
			return null;
		else
			return fstage.getResourceProperty();
	}
	
    public void setSize(int width, int height) {
        FlyerProperty parent = fstage.getResourceProperty();
        DoubleProperty property = parent.updateOrCreateDoubleProperty(FlyerProperty.NameFlyerWidth, width);
        updatePrimeProperty(parent, property);
        
        property = parent.updateOrCreateDoubleProperty(FlyerProperty.NameFlyerHeight, height);
        updatePrimeProperty(parent, property);
    }

    public boolean updatePrimeProperty(NodeProperty parent, PrimeProperty rp) {
		boolean result = fstage.updatePrimeProperty(parent, rp);
		if(parent.equals(fstage.getResourceProperty())) {
			if(rp.getName().equals(FlyerProperty.NameFlyerWidth)) {
			    double w = ((DoubleProperty)rp).getValue();
			    getElement().getStyle().setWidth(w, Unit.PX);
			}
			else if(rp.getName().equals(FlyerProperty.NameFlyerHeight)) {
			    double h = ((DoubleProperty)rp).getValue();
			    getElement().getStyle().setHeight(h, Unit.PX);
			}
		}
		return result;
    }
    
	public void selectNode(ResourceProperty nodeProperty) {
		fstage.selectNode(nodeProperty);
    }

    public void reset() {
		if(fstage != null)
			fstage.reset();
    }

	public void deleteShape(ShapeProperty sp) {
		fstage.deleteShape(sp);
    }

	public Flayer createLayer() {
	    return fstage.createLayer();
    }

    public Fnode createNewNodeInCurrentLayer(String class1, NodeProperty np) {
        if(fstage == null) {
            createStage(600, 480);
            createLayer();
        }
        return fstage.createNewNodeInCurrentLayer(class1, np);
    }

    public Fnode createNewNode(String class1, LayerProperty lp, NodeProperty np) {
        return fstage.createNewNode(class1, lp, np);
    }

	public boolean moveDown(LayerProperty layer, NodeProperty node) {
		return fstage.moveDown(layer, node);
    }

	public boolean moveUp(LayerProperty layer, NodeProperty node) {
		return fstage.moveUp(layer, node);
    }

	public void setDisplayMode(DisplayMode mode) {
		this.mode  = mode;
		if(fstage != null)
			fstage.setMode(mode);
	}
	
	public DisplayMode getDisplayMode() {
	    return mode;
	}
	
	public void getImage(final StageDataCallback callback) {
		fstage.getImage(new net.edzard.kinetic.Stage.DataUrlTarget(){
			@Override
            public void receive(String url) {
				callback.receive(url);
            }});
	}

	public void onFontsLoaded() {
		try {
            load(getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void onImgLoaded() {
		if(fstage != null)
			fstage.onImgLoaded();
    }

	public void zoomIn(float scaleValue) {
		fstage.onZoomin(scaleValue);
		Vector2d scale = fstage.getNode().getScale();
		getElement().getStyle().setWidth(fstage.getResourceProperty().getIntWidth() * scale.x, Unit.PX);
		getElement().getStyle().setHeight(fstage.getResourceProperty().getIntHeight() * scale.y, Unit.PX);
    }

	public void zoomOut(float scaleValue) {
		fstage.onZoomout(scaleValue);
		Vector2d scale = fstage.getNode().getScale();
		getElement().getStyle().setWidth(fstage.getResourceProperty().getIntWidth() * scale.x, Unit.PX);
		getElement().getStyle().setHeight(fstage.getResourceProperty().getIntHeight() * scale.y, Unit.PX);
    }
}
