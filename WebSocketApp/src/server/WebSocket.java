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
import model.ConnectedUser;
import model.DisconnectedUser;
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
        sendUserToAllClients(connectedUserInfos.get(session),false);
        connectedUserInfos.remove(session);
        clients.remove(session);
    }
    
	@SuppressWarnings("unchecked")
	@OnMessage(maxMessageSize = 6024000)
    public void handleMessage(Session session,@SuppressWarnings("rawtypes") WebSocketMessage webSocketMessage) {
		Message<?> message = null;
    	if (webSocketMessage.getPayload() instanceof Message) {
    		message = (Message<?>) webSocketMessage.getPayload();
   			webSocketMessage.setPayload(message);
   			
   			for(Session client : clients){
   	    		if(connectedUserInfos.get(client).equalsIgnoreCase(message.getReceiver()) || connectedUserInfos.get(client).equalsIgnoreCase(message.getSender()))
   	    			client.getAsyncRemote().sendObject(webSocketMessage);
   	        }
   		}
   		if(webSocketMessage.getPayload() instanceof ConnectedUser) { 
   			ConnectedUser newUser = (ConnectedUser) webSocketMessage.getPayload();
    		connectedUserInfos.put(session, newUser.getUsername());
    		for(Session client : clients){
    			sendUserToAllClients(connectedUserInfos.get(client),true);
    			
    		}
   		}    	
    }
     
    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
    
    public void sendUserToAllClients(String user, Boolean status) {
    	if(status == true) { // it means "Online"
    		ConnectedUser connectedUser = new ConnectedUser(user);
    		WebSocketMessage<ConnectedUser> webSocketMessage = new WebSocketMessage<ConnectedUser>(connectedUser);
    		
    		for(Session client : clients){
    			client.getAsyncRemote().sendObject(webSocketMessage);
            }
    	}
    	else if(status == false){ // it means "Offline"
    		DisconnectedUser disconnectedUser = new DisconnectedUser(user);
    		WebSocketMessage<DisconnectedUser> webSocketMessage = new WebSocketMessage<DisconnectedUser>(disconnectedUser);
    		
    		for(Session client : clients){
    			client.getAsyncRemote().sendObject(webSocketMessage);
            }
    	}
		
    }
}
