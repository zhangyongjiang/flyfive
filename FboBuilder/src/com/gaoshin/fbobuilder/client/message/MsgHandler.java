package com.gaoshin.fbobuilder.client.message;

public interface MsgHandler<T extends Message> {

	void processMsg(T msg);

}
