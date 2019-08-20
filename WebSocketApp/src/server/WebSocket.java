package server;

import java.util.Date;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import model.Message;

@ServerEndpoint("/chat")
public class WebSocket {
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());       
        
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
    }
    
    @OnMessage
    public void handleTextMessage(Session session,String message) {
    	Gson gson = new Gson();
    	Message messageFromClient = gson.fromJson(message, Message.class);
    	System.out.println(messageFromClient.getSender() + " : " + messageFromClient.getContent());
    	System.out.println("Delivery time : " + new Date());
    	Message messageFromServer = new Message();
    	messageFromServer.setSender("Server");
    	messageFromServer.setContent("What's up");
    	messageFromServer.setReceivedDate(new Date());
    	

    	String messageJson = gson.toJson(messageFromServer);
    	session.getAsyncRemote().sendText(messageJson);
    }
     
    @OnMessage(maxMessageSize = 1024000)
    public byte[] handleBinaryMessage(byte[] buffer) {
        System.out.println("New Binary Message Received");
        return buffer;
    }
    
    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
}
