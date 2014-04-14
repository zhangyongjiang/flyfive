package com.flyerfive.client.data;

public class FileProperty extends ResourceProperty {
	private String permission;
	
	public FileProperty() {
		setType(PropertyType.File);
    }

	public String getPermission() {
	    return permission;
    }

	public void setPermission(String permission) {
	    this.permission = permission;
    }
	
	public String getFileType() {
		int pos = getName().lastIndexOf(".");
		if(pos == -1)
			return null;
		return getName().substring(pos + 1);
	}
}
