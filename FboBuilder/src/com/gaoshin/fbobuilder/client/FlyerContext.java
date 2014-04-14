package com.gaoshin.fbobuilder.client;

import com.gaoshin.fbobuilder.client.message.MessageBus;

public class FlyerContext {
	private static MessageBus mb = new MessageBus();

	public static MessageBus getMb() {
	    return mb;
    }

}
