package model;

import java.io.Serializable;

public class WebSocketMessage<T>  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private T payload;

	public WebSocketMessage() {

	}

	public WebSocketMessage(T payload) {
		this.payload = payload;
		if (payload instanceof Message) {
			this.setType(Message.type);
		} else if (payload instanceof ConnectedUser) {
			this.setType(ConnectedUser.type);
		} else if (payload instanceof DisconnectedUser) {
			this.setType(DisconnectedUser.type);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

}