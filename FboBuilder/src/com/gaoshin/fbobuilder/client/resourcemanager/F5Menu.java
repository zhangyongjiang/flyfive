package com.gaoshin.fbobuilder.client.resourcemanager;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class F5Menu extends Menu {
	public static interface MenuItemSelectedListener {
		void onMenuItemSelected(String fontFamily);
	}
	
	protected MenuItemSelectedListener listener;
	
	public void setListener(MenuItemSelectedListener listener) {
		this.listener = listener;
	}

	public void addMenu(String text, boolean enable, ImageResource icon, SelectionHandler sh) {
		MenuItem menuItem = new MenuItem();
		menuItem.setEnabled(enable);
		menuItem.setText(text);
		menuItem.setIcon(icon);
		menuItem.addSelectionHandler(sh);
		add(menuItem);
	}

	public void addMenu(String text, boolean enable, ImageResource icon) {
		addMenu(text, enable, icon, new SelectionHandler() {
			@Override
            public void onSelection(SelectionEvent event) {
				String text = ((MenuItem)event.getSelectedItem()).getText();
				if(listener != null)
					listener.onMenuItemSelected(text);
            }
		});
	}
}
