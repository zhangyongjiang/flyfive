package com.gaoshin.fbobuilder.client.data;

public class FlyerFolderProperty extends ResourceProperty {
	private String permission = "700";
	
	public FlyerFolderProperty() {
		setType(PropertyType.FlyerFolder);
    }

	public String getPermission() {
	    return permission;
    }

	public void setPermission(String permission) {
	    this.permission = permission;
    }
	
	public void setPublic() {
		permission = "644";
	}
	
	public void setPrivate() {
		permission = "600";
	}
}
