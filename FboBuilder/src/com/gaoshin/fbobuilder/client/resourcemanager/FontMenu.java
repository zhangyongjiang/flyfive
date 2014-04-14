package com.gaoshin.fbobuilder.client.resourcemanager;


public class FontMenu extends F5Menu {
	public static final String[] fontFamilies = {
		"Arial,sans-serif",
		"'Arial Black',Gadget,sans-serif",
		"'Comic Sans MS',cursive",
		"'Monotype Sorts',dingbats,'ITC Zapf Dingbats',fantasy",
		"Tahoma,sans-serif",
		"Bonbon",
		"Cambria,'Times New Roman','Nimbus Roman No9 L','Freeserif',Times,serif",
		"Clicker Script",
		"Codystar",
		"Consolas,'Lucida Console','DejaVu Sans Mono',monospace",
		"Constantia,Georgia,'Nimbus Roman No9 L',serif",
		"Ewert",
		"Freckle Face",
		"Grand Hotel",
		"Hanalei",
		"Herr Von Muellerhoff",
		"Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif",
		"Merienda",
		"Monoton",
		"Mrs Saint Delafield",
		"Mystery Quest",
		"New Rocker",
		"Princess Sofia",
		"Qwigley",
		"Ribeye Marrow",
		"Sacramento",
		"Seymour One",
		"Sofia",
		"Sonsie One",
		"Tangerine",
		"Webdings,fantasy",
		"Wingdings,fantasy",
    };
	
	public FontMenu() {
		for (String family : fontFamilies) {
			addMenu(family, true, null);
		}
	}
}
