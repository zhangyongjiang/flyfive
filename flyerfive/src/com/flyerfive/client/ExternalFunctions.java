package com.flyerfive.client;

public class ExternalFunctions {
    public static native void loadImg(String url) /*-{
        $wnd.loadImg(url);
    }-*/;

    public static native void loadFont(String font, String variant, String subset) /*-{
        $wnd.loadFont(font, variant, subset);
    }-*/;

}
