package com.flyerfive.client.data;

import java.util.ArrayList;
import java.util.List;

import com.flyerfive.client.font.GoogleFontFamily;
import com.flyerfive.client.font.GoogleFontFamilyList;

public class FontFamilyProperty extends SelectProperty<String> {

    @Override
    public List<String> getOptions() {
        List<String> options = new ArrayList<String>();
        GoogleFontFamilyList fontFamilyList = GoogleFontFamilyList.getFontFamilyList();
        for(GoogleFontFamily ff : fontFamilyList.getItems()) {
            options.add(ff.getFamily());
        }
        return options;
    }

    @Override
    public void setStringValue(String value) {
        setValue(value);
    }
    
}
