package server;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import model.MessageEncoder;
import model.WebSocketMessage;
import model.AllConnectedUsers;
import model.ConnectedUser;
import model.Message;
import model.MessageDecoder;

@ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class WebSocket {
	
	static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	static Map<Session,String> connectedUserInfos = Collections.synchronizedMap(new HashMap<Session,String>());
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());       
        clients.add(session);
    }
    
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
        clients.remove(session);
        connectedUserInfos.remove(session);
        sendOnlineUsersToAllClients(connectedUserInfos);
    }
    
	@SuppressWarnings("unchecked")
	@OnMessage
    public void handleMessage(Session session,@SuppressWarnings("rawtypes") WebSocketMessage webSocketMessage) {
		Message textMessage = null;
    	if (webSocketMessage.getPayload() instanceof Message) {
    		textMessage = (Message) webSocketMessage.getPayload();
   			webSocketMessage.setPayload(textMessage);
   			
   			for(Session client : clients){
   	    		if(connectedUserInfos.get(client).equalsIgnoreCase(textMessage.getReceiver()) || connectedUserInfos.get(client).equalsIgnoreCase(textMessage.getSender()))
   	    			client.getAsyncRemote().sendObject(webSocketMessage);
   	        }
   		}
   		if(webSocketMessage.getPayload() instanceof ConnectedUser) { 
   			ConnectedUser newUser = (ConnectedUser) webSocketMessage.getPayload();
    		connectedUserInfos.put(session, newUser.getUsername());
    		sendOnlineUsersToAllClients(connectedUserInfos);
   		}    	
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
    
    public void sendOnlineUsersToAllClients(Map<Session,String> connectedUserInfos) {
    	Set<String> connectedUserSet = new HashSet<String>(connectedUserInfos.values());
		AllConnectedUsers allConnectedUsers = new AllConnectedUsers(connectedUserSet);
		WebSocketMessage<AllConnectedUsers> webSocketMessage = new WebSocketMessage<AllConnectedUsers>(allConnectedUsers);
		for(Session client : clients){
			client.getAsyncRemote().sendObject(webSocketMessage);
        }
    }
}
