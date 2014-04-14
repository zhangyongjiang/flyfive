package com.gaoshin.fbobuilder.client.resourcemanager;

import com.gaoshin.fbobuilder.client.data.FlyerFolderProperty;


public class ResourceData {
	public static FlyerFolderProperty getFliers() {
		FlyerFolderProperty root = new FlyerFolderProperty();
		
		FlyerFolderProperty all = new FlyerFolderProperty();
		all.setName("All");
		root.getChildren().add(all);
		
		FlyerFolderProperty ap = new FlyerFolderProperty();
		ap.setName("Examples");
		all.getChildren().add(ap);
		
		return root;
	}
	
	public static final native String saveFlyer(String json) /*-{
		return $wnd.saveFlyer(json);
	}-*/;
	
	public static final native boolean removeFlyer(String id) /*-{
		return $wnd.removeFlyer(id);
	}-*/;

}
