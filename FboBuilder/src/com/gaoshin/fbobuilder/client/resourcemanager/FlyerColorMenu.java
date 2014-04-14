package com.gaoshin.fbobuilder.client.resourcemanager;

import net.edzard.kinetic.Colour;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.menu.ColorMenu;

public class FlyerColorMenu extends ColorMenu {
	public static interface ColorChangedListener {
		void onColorChange(Colour color);
	}
	
	private ColorChangedListener listener;
	
	@Override
	protected void onValueChanged(ValueChangeEvent<String> event) {
		if(listener != null) 
			listener.onColorChange(getSelectedColor());
	}
	
	@Override
	protected void onClick(Event ce) {
	    super.onClick(ce);
		if(listener != null) 
			listener.onColorChange(getSelectedColor());
	}

	public void setListener(ColorChangedListener colorChangedListener) {
		this.listener = colorChangedListener;
    }
	
	public Colour getSelectedColor() {
		String color = getColor();
		return new Colour("#" + color);
	}
}
