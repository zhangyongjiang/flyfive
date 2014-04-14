package com.gaoshin.fbobuilder.client.resourcemanager;

import net.edzard.kinetic.Text.HorizontalAlignment;

public class TextAlignMenu extends F5Menu {
	public TextAlignMenu() {
		for(HorizontalAlignment join : HorizontalAlignment.values()) {
			addMenu(join.name(), true, null);
		}
    }
}
