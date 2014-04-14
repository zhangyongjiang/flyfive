package com.flyerfive.client.data;

import java.util.ArrayList;
import java.util.List;

public abstract class EnumProperty<T> extends SelectProperty<T> {
    protected Class<T> enumCls;
    
    public EnumProperty(Class enumCls) {
        this.enumCls = enumCls;
    }
    
    @Override
    public List<String> getOptions() {
        List<String> options = new ArrayList<String>();
        for(Object obj : enumCls.getEnumConstants()) {
            options.add(obj.toString());
        }
        return options;
    }

    public void setStringValue(String value) {
        for(Object o : enumCls.getEnumConstants()) {
            if(o.toString().equals(value)) {
                setValue((T) o);
                return;
            }
        }
    }
}
