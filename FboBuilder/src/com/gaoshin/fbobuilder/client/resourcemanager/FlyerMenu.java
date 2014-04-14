package com.gaoshin.fbobuilder.client.resourcemanager;

import com.gaoshin.fbobuilder.client.FlyerContext;
import com.gaoshin.fbobuilder.client.message.RemoveMsg;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.widget.core.client.menu.Item;

public class FlyerMenu extends F5Menu {
	public FlyerMenu() {
		addMenu("Remove", true, ExampleImages.INSTANCE.delete(), new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new RemoveMsg());
			}
		});
    }
}
