package net.auroris.ColorPicker.client;

public interface ColorListener {
    void onColor(String color);
    void onCancel();
    void onOk(String color);
}
