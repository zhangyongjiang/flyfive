package com.gaoshin.fbobuilder.client.resourcemanager;

import com.gaoshin.fbobuilder.client.FlyerContext;
import com.gaoshin.fbobuilder.client.data.FlyerFolderProperty;
import com.gaoshin.fbobuilder.client.message.Message;
import com.gaoshin.fbobuilder.client.message.MsgHandler;
import com.gaoshin.fbobuilder.client.message.NewFlyerMsg;
import com.gaoshin.fbobuilder.client.message.NewFolderMsg;
import com.gaoshin.fbobuilder.client.message.PasteMsg;
import com.gaoshin.fbobuilder.client.message.RefreshMsg;
import com.gaoshin.fbobuilder.client.message.SaveFlyerFolderMsg;
import com.gaoshin.fbobuilder.client.message.ShareMsg;
import com.gaoshin.fbobuilder.client.message.TreeItemChangedMsg;
import com.gaoshin.fbobuilder.client.message.UnshareMsg;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class FlyerFolderContextMenu extends FlyerMenu {
	public static class RenameFolderMsg extends Message {}
	
	private MenuItem newFlyer;
	private MenuItem newFolder;
	private MenuItem rename;
	private MenuItem paste;
	private MenuItem save;
	private MenuItem refresh;
	private MenuItem permissionMenu;

	public FlyerFolderContextMenu() {
		newFlyer = new MenuItem();
		newFlyer.setText("New Flyer");
		newFlyer.setIcon(ExampleImages.INSTANCE.add());
		newFlyer.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new NewFlyerMsg());
			}
		});
		add(newFlyer);

		newFolder = new MenuItem();
		newFolder.setText("New Folder");
		newFolder.setIcon(ExampleImages.INSTANCE.add());
		newFolder.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new NewFolderMsg());
			}
		});
		add(newFolder);

		rename = new MenuItem();
		rename.setEnabled(true);
		rename.setText("Rename");
		rename.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new RenameFolderMsg());
			}
		});
		add(rename);

		paste = new MenuItem();
		paste.setEnabled(true);
		paste.setText("Paste");
		paste.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new PasteMsg());
			}
		});
		add(paste);
		
		save = new MenuItem();
		save.setEnabled(true);
		save.setText("Save");
		save.setIcon(ExampleImages.INSTANCE.delete());
		save.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new SaveFlyerFolderMsg());
			}

		});
		add(save);

		refresh = new MenuItem();
		refresh.setEnabled(true);
		refresh.setText("Refresh");
		refresh.setIcon(ExampleImages.INSTANCE.json());
		refresh.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new RefreshMsg());
			}

		});
		add(refresh);

		permissionMenu = new MenuItem();
		permissionMenu.setEnabled(true);
		permissionMenu.setText("Share");
		permissionMenu.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				if(permissionMenu.getText().endsWith("Share"))
					FlyerContext.getMb().sendMsg(new ShareMsg());
				else
					FlyerContext.getMb().sendMsg(new UnshareMsg());
			}
		});
		add(permissionMenu);
		
		setupItemSelectedListener();
    }

	private void setupItemSelectedListener() {
		FlyerContext.getMb().registerHandler(TreeItemChangedMsg.class, new MsgHandler<TreeItemChangedMsg>() {
			@Override
            public void processMsg(TreeItemChangedMsg msg) {
				if(!(msg.getItem() instanceof FlyerFolderProperty))
					return;
				FlyerFolderProperty ffp = (FlyerFolderProperty)msg.getItem();
				String permission = ffp.getPermission();
				if(permission.charAt(1) == '0') {
					permissionMenu.setText("Share");
				}
				else {
					permissionMenu.setText("Unshare");
				}
            }
		});
    }

}
