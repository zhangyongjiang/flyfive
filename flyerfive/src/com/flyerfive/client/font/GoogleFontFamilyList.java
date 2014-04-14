package com.flyerfive.client.font;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;

public class GoogleFontFamilyList extends JavaScriptObject {
    protected GoogleFontFamilyList() {}

    public final native String getKind() /*-{
        return this.kind;
    }-*/;

    public final native List<GoogleFontFamily> getItems() /*-{
        var result = @java.util.ArrayList::new()();
        for (i=0; i < this.items.length; ++i) {
            result.@java.util.ArrayList::add(Ljava/lang/Object;)(this.items[i]);
        }
        return result;
    }-*/;

    public static final native GoogleFontFamilyList getFontFamilyList() /*-{
        return $wnd.FontFamilyList;
    }-*/;
}
