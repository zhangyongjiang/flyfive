package com.flyerfive.client.model;

import net.edzard.kinetic.Vector2d;

public class MarkedPosition<T> {
	private Vector2d originalNodePos;
	private T id;
	private Vector2d pos;

	public MarkedPosition(Vector2d nodePos, T id, Vector2d pos) {
		originalNodePos = nodePos;
		this.id = id;
		this.pos = pos;
    }
	
	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public Vector2d getPos() {
		return pos;
	}

	public void setPos(Vector2d pos) {
		this.pos = pos;
	}

	public Vector2d getOriginalNodePos() {
	    return originalNodePos;
    }

	public void setOriginalNodePos(Vector2d originalNodePos) {
	    this.originalNodePos = originalNodePos;
    }

}
