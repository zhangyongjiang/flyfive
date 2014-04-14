package com.gaoshin.fbobuilder.client.resourcemanager;

public abstract class ResourceViewer {
	protected ResourceManager rm;
	
	public ResourceViewer(ResourceManager rm) {
		this.rm = rm;
    }
}
