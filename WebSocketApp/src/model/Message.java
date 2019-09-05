package model;

import java.util.Date;

@SuppressWarnings("rawtypes")
public class Message extends WebSocketMessage{

	public static final String type = "message";
    private String content;
    private String sender;
    private String receiver;
    private Date receivedDate;
  
    
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
}