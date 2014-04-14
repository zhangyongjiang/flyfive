package com.flyerfive.client.data;

import com.flyerfive.client.model.FtextPath;

public class SpriteProperty extends ShapeProperty<FtextPath> {
	
	public SpriteProperty() {
		setType(PropertyType.Sprite);
    }
	
	@Override
	public void fromFnode(FtextPath fshape) {
	    super.fromFnode(fshape);
	}
}
