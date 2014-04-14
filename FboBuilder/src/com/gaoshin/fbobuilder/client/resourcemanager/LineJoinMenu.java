package com.gaoshin.fbobuilder.client.resourcemanager;

import net.edzard.kinetic.Shape.LineJoin;

public class LineJoinMenu extends F5Menu {
	public LineJoinMenu() {
		for(LineJoin join : LineJoin.values()) {
			addMenu(join.name(), true, null);
		}
    }
}
