package com.gaoshin.fbobuilder.client.resourcemanager;

import com.gaoshin.fbobuilder.client.FlyerContext;
import com.gaoshin.fbobuilder.client.message.DownMsg;
import com.gaoshin.fbobuilder.client.message.UpMsg;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class NodeContextMenu extends Menu {
	private MenuItem up;
	private MenuItem down;

	public NodeContextMenu() {
		up = new MenuItem();
		up.setText("Move down to back");
		up.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new UpMsg());
			}
		});
		add(up);

		down = new MenuItem();
		down.setEnabled(true);
		down.setText("Bring up to front");
		down.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new DownMsg());
			}

		});
		add(down);

    }
}
