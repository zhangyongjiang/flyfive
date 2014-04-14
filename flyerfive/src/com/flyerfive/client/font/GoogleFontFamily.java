package com.flyerfive.client.font;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;

public class GoogleFontFamily extends JavaScriptObject {
    protected GoogleFontFamily() {}

    public final native String getKind() /*-{
        return this.kind;
    }-*/;

    public final native String getFamily() /*-{
        return this.family;
    }-*/;

    public final native List<String> getVariants() /*-{
        var result = @java.util.ArrayList::new()();
        for (i=0; i < this.variants.length; ++i) {
            result.@java.util.ArrayList::add(Ljava/lang/Object;)(this.variants[i]);
        }
        return result;
    }-*/;

    public final native List<String> getSubsets() /*-{
        var result = @java.util.ArrayList::new()();
        for (i=0; i < this.subsets.length; ++i) {
            result.@java.util.ArrayList::add(Ljava/lang/Object;)(this.subsets[i]);
        }
        return result;
    }-*/;
}
