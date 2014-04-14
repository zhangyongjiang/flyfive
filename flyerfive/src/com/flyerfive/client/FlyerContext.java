package com.flyerfive.client;

import java.util.HashMap;
import java.util.Map;

import com.flyerfive.client.message.Message;
import com.flyerfive.client.message.MessageBus;
import com.flyerfive.client.model.Fstage;

public class FlyerContext {
    public static class DefaultValueMsg<T> extends Message {
        private String  key;
        private T value;
        
        public DefaultValueMsg(String key, T value) {
            this.key = key;
            this.value = value;
        }
        
        public String getKey() {
            return key;
        }
        
        public T getValue() {
            return value;
        }
    }
    
	private MessageBus mb;
	private Map<String, Object> defaultValues;
    private Fstage fstage;

	public FlyerContext() {
	    mb = new MessageBus();
	    defaultValues = new HashMap<String, Object>();
    }
	
    public MessageBus getMb() {
	    return mb;
    }

    public Object getDefaultValue(String key) {
        return defaultValues.get(key);
    }

    public void setDefaultValue(String key, Object value) {
        this.defaultValues.put(key, value);
    }

}
