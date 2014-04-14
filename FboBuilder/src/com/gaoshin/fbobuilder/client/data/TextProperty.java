package com.gaoshin.fbobuilder.client.data;

import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Text;
import net.edzard.kinetic.Text.HorizontalAlignment;

import com.gaoshin.fbobuilder.client.model.Ftext;
import com.google.gwt.json.client.JSONObject;

public class TextProperty extends ShapeProperty<Ftext> {
	public static final String NameText = "text";
	public static final String NameFontFamily = "font-family";
	public static final String NameFontSize = "font-size";
	public static final String NameTextColor = "text.color";
	public static final String NameWidth = "width";
	public static final String NameHeight = "height";
	public static final String NameAlign = "align";
	
	public TextProperty() {
		setType(PropertyType.Text);
    }
	
	@Override
	public void fromFnode(Ftext fshape) {
	    super.fromFnode(fshape);
	    updateOrCreateStrProperty(NameText, fshape.getNode().getText());
		updateOrCreateFontFamilyProperty(NameFontFamily, fshape.getNode().getFontFamily());
		updateOrCreateIntProperty(NameFontSize, fshape.getNode().getFontSize());
		updateOrCreateDoubleProperty(NameWidth, fshape.getNode().getWidth());
		updateOrCreateTextAlignProperty(NameAlign, fshape.getNode().getHorizontalAlignment());
		
		Text text = (Text)fshape.getNode();
		Colour txtFill = text.getTextFill();
		addColorProperty(NameTextColor, txtFill);
	}

	@Override
	public void toFnode(Ftext fnode) {
	    super.toFnode(fnode);
	    String text = getStrProperty(NameText);
	    fnode.getNode().setText(text);
	    
	    String family = getStrProperty(NameFontFamily);
	    fnode.getNode().setFontFamily(family);
	    
	    HorizontalAlignment align = getTextAlignProperty(NameAlign);
	    if(align != null)
	    	fnode.getNode().setHorizontalAlignment(align);
	    else
	    	fnode.getNode().setHorizontalAlignment(HorizontalAlignment.LEFT);
	    
	    int fontSize = getIntProperty(NameFontSize);
	    fnode.getNode().setFontSize(fontSize);
	    
	    double w = getDoubleProperty(NameWidth);
	    fnode.getNode().setWidth(w);
	    
	    Colour txtFill = getColorProperty(NameTextColor); 
	    fnode.getNode().setTextFill(txtFill);
	}
	
	@Override
	public boolean updateView(Ftext fnode, PrimeProperty rp) {
	    boolean found = super.updateView(fnode, rp);
		if(found) return found;
		
		if(rp.getName().equals(NameText)) {
			String text = getStrProperty(NameText);
		    fnode.getNode().setText(text);
		    setName(text);
			found = true;
		}
		else if(rp.getName().equals(NameFontFamily)) {
			String text = getStrProperty(NameFontFamily);
		    fnode.getNode().setFontFamily(text);
			found = true;
		}
		else if(rp.getName().equals(NameAlign)) {
		    fnode.getNode().setHorizontalAlignment(getTextAlignProperty(NameAlign));
			found = true;
		}
		else if(rp.getName().equals(NameFontSize)) {
		    int fontSize = getIntProperty(NameFontSize);
		    fnode.getNode().setFontSize(fontSize);
			found = true;
		}
		else if(rp.getName().equals(NameWidth)) {
		    double w = getDoubleProperty(NameWidth);
		    fnode.getNode().setWidth(w);
			found = true;
		}
		else if(rp.getName().equals(NameTextColor)) {
		    Colour value = getColorProperty(NameTextColor);
		    fnode.getNode().setTextFill(value);
			found = true;
		}
		
		return found;
	}
	
	@Override
	public String getName() {
		String text = getStrProperty(NameText);
		return text;
	}
	
	public void setText(String txt) {
		updateOrCreateStrProperty(NameText, txt);
	}
	
	@Override
	public void fromJsonObject(JSONObject jobj) throws Exception {
	    super.fromJsonObject(jobj);
	}
}
