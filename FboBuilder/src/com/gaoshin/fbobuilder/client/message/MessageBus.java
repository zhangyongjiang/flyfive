package com.gaoshin.fbobuilder.client.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageBus {
	private Map<Class, List<MsgHandler>> handlers;
	
	public MessageBus() {
		handlers = new HashMap<Class, List<MsgHandler>>();
    }
	
	public <T extends Message> void registerHandler(Class<T> cls, MsgHandler<T> handler) {
		List<MsgHandler> list = handlers.get(cls);
		if(list == null) {
			list = new ArrayList<MsgHandler>();
			handlers.put(cls, list);
		}
		list.add(handler);
	}
	
	public void sendMsg(Message msg) {
		List<MsgHandler> list = handlers.get(msg.getClass());
		if(list == null)
			return;
		for(MsgHandler h : list) {
			h.processMsg(msg);
		}
	}
}
