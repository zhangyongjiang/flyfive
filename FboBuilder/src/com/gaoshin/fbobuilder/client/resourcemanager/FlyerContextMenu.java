package com.gaoshin.fbobuilder.client.resourcemanager;

import com.gaoshin.fbobuilder.client.FlyerContext;
import com.gaoshin.fbobuilder.client.data.FlyerProperty;
import com.gaoshin.fbobuilder.client.message.EmbedMsg;
import com.gaoshin.fbobuilder.client.message.ExportImgMsg;
import com.gaoshin.fbobuilder.client.message.LinkMsg;
import com.gaoshin.fbobuilder.client.message.MsgHandler;
import com.gaoshin.fbobuilder.client.message.NewLayerMsg;
import com.gaoshin.fbobuilder.client.message.PostToFbMsg;
import com.gaoshin.fbobuilder.client.message.RefreshMsg;
import com.gaoshin.fbobuilder.client.message.SaveFlyerMsg;
import com.gaoshin.fbobuilder.client.message.ShareMsg;
import com.gaoshin.fbobuilder.client.message.TreeItemChangedMsg;
import com.gaoshin.fbobuilder.client.message.UnshareMsg;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class FlyerContextMenu extends FlyerMenu {
	private MenuItem insert;
	private MenuItem save;
	private MenuItem postToFb;
	private MenuItem refresh;
	private MenuItem permissionMenu;
	private MenuItem link;
	private MenuItem embed;
	private MenuItem exportImg;

	public FlyerContextMenu() {
		insert = new MenuItem();
		insert.setText("Add Layer");
		insert.setIcon(ExampleImages.INSTANCE.add());
		insert.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new NewLayerMsg());
			}
		});
		add(insert);

		save = new MenuItem();
		save.setEnabled(true);
		save.setText("Save");
		save.setIcon(ExampleImages.INSTANCE.delete());
		save.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new SaveFlyerMsg());
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
		permissionMenu.setText("Make Public");
		permissionMenu.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				if(permissionMenu.getText().endsWith("Make Public"))
					FlyerContext.getMb().sendMsg(new ShareMsg());
				else
					FlyerContext.getMb().sendMsg(new UnshareMsg());
			}
		});
		add(permissionMenu);

		link = new MenuItem();
		link.setEnabled(true);
		link.setText("Export Link");
		link.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new LinkMsg());
			}
		});
		add(link);

		embed = new MenuItem();
		embed.setEnabled(true);
		embed.setText("Export Embed Code");
		embed.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new EmbedMsg());
			}
		});
		add(embed);
		
		exportImg = new MenuItem();
		exportImg.setEnabled(true);
		exportImg.setText("Save as Image");
		exportImg.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new ExportImgMsg());
			}
		});
		add(exportImg);
		
		postToFb = new MenuItem();
		postToFb.setEnabled(true);
		postToFb.setText("Post to my Facebook");
		postToFb.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				FlyerContext.getMb().sendMsg(new PostToFbMsg());
			}
		});
		add(postToFb);
		
		setupItemSelectedListener();
    }

	private void setupItemSelectedListener() {
		FlyerContext.getMb().registerHandler(TreeItemChangedMsg.class, new MsgHandler<TreeItemChangedMsg>() {
			@Override
            public void processMsg(TreeItemChangedMsg msg) {
				if(!(msg.getItem() instanceof FlyerProperty))
					return;
				FlyerProperty ffp = (FlyerProperty)msg.getItem();
				String permission = ffp.getPermission();
				if(permission.charAt(1) == '0') {
					permissionMenu.setText("Make Public");
				}
				else {
					permissionMenu.setText("Make Private");
				}
            }
		});
    }
}
