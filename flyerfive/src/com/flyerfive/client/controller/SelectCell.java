package com.flyerfive.client.controller;

import java.util.List;

import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.data.SelectProperty;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class SelectCell<T extends SelectProperty> extends PropertyCell<T> implements IsWidget {
    private ListBox listBox;
    private ChangeHandler changeHandler;
    
    public SelectCell(T prop, FlyerContext ctx) {
        super(prop, ctx);
    }

    @Override
    public Widget asWidget() {
        listBox = new ListBox();
        int selected = -1;
        List<String> options = property.getOptions();
        for(int i=0; i<options.size(); i++) {
            String item = options.get(i);
            listBox.addItem(item);
            if(item.equals(getOriginalValue()))
                selected = i;
        }
        if(selected >= 0)
            listBox.setSelectedIndex(selected);
        listBox.addChangeHandler(changeHandler);
        return listBox;
    }

    @Override
    public void updateView() {
        setOriginalValue(property.getStringValue());
    }

    public ChangeHandler getChangeHandler() {
        return changeHandler;
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public String getValue() {
        return listBox.getItemText(listBox.getSelectedIndex());
    }
}
