package com.gaoshin.fbobuilder.client;

import com.gaoshin.fbobuilder.client.resourcemanager.ResourceManager;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.desktopapp.client.canvas.CanvasToolBar;
import com.sencha.gxt.desktopapp.client.canvas.CanvasViewImpl;
import com.sencha.gxt.state.client.BorderLayoutStateHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class FlierBuilderBorderLayoutContainer extends BorderLayoutContainer {
	private CanvasViewImpl canvasImpl;
	
	public CanvasViewImpl getCanvas() {
		return canvasImpl;
	}
	
	public FlierBuilderBorderLayoutContainer() {
		monitorWindowResize = true;
		Window.enableScrolling(false);
		setPixelSize(Window.getClientWidth(), Window.getClientHeight());

		setStateful(true);
		setStateId("explorerLayout");

		BorderLayoutStateHandler state = new BorderLayoutStateHandler(this);
		state.loadState();

	    StringBuffer sb = new StringBuffer();
	    sb.append("<div id='demo-theme'></div><div id=demo-title>Sencha GXT Explorer Demo</div>");

	    ListStore<Theme> colors = new ListStore<Theme>(new ModelKeyProvider<Theme>() {
	      @Override
	      public String getKey(Theme item) {
	        return item.name();
	      }

	    });
	    colors.add(Theme.BLUE);
	    colors.add(Theme.GRAY);

	    final SimpleContainer con = new SimpleContainer();
	    con.getElement().getStyle().setMargin(3, Unit.PX);
	    con.setResize(false);

	    ComboBox<Theme> combo = new ComboBox<Theme>(colors, new StringLabelProvider<Theme>());
	    combo.setTriggerAction(TriggerAction.ALL);
	    combo.setForceSelection(true);
	    combo.setEditable(false);
	    combo.getElement().getStyle().setFloat(Float.RIGHT);
	    combo.setWidth(125);
	    combo.setValue(Theme.GRAY);
	    combo.addSelectionHandler(new SelectionHandler<Theme>() {

	      @Override
	      public void onSelection(SelectionEvent<Theme> event) {
	        if (event.getSelectedItem() == Theme.BLUE) {
	          Window.Location.replace("explorer-blue.html");
	        } else {
	          Window.Location.replace("explorer-gray.html");
	        }
	      }
	    });

	    con.add(combo);

	    // not adding selector now
	    
	    canvasImpl = new CanvasViewImpl();
		ResourceManager tree = new ResourceManager(canvasImpl);
	    CanvasToolBar toolbar = new CanvasToolBar();
	    
	    BorderLayoutData topData = new BorderLayoutData(36);
	    topData.setMargins(new Margins(0, 0, 0, 0));
	    topData.setSplit(false);
	    topData.setCollapsible(false);
	    topData.setCollapseHidden(false);
	    topData.setCollapseMini(false);
	    setNorthWidget(toolbar, topData);


	    BorderLayoutData westData = new BorderLayoutData(300);
	    westData.setMargins(new Margins(0, 0, 5, 5));
	    westData.setSplit(true);
	    westData.setCollapsible(false);
	    westData.setCollapseHidden(false);
	    westData.setCollapseMini(false);

	    west = new ContentPanel();
	    west.setHeaderVisible(false);
	    west.setBodyBorder(true);
	    west.add(tree);

	    MarginData centerData = new MarginData();
	    centerData.setMargins(new Margins(0,0,0,4));

	    VerticalLayoutContainer center = new VerticalLayoutContainer();
	    center.setBorders(false);
	    center.add(canvasImpl.getContainer());
		center.setScrollMode(ScrollMode.AUTO);
		
	    setWestWidget(west, westData);
	    setCenterWidget(center, centerData);
	}

	private ContentPanel west;

	enum Theme {
		BLUE("Blue Theme"), GRAY("Gray Theme");

		private final String value;

		private Theme(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}

		@Override
		public String toString() {
			return value();
		}
	}

	@Override
	protected void onWindowResize(int width, int height) {
		setPixelSize(width, height);
	}
}
