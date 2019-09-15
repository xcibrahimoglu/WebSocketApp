package server;

import java.util.Collection;
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
	
	static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	static Map<Session,String> connectedUserInfos = Collections.synchronizedMap(new HashMap<Session,String>());
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());       
        peers.add(session);
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
        peers.remove(session);
        connectedUserInfos.remove(session);
        Set<String> connectedUserSet = new HashSet<String>(connectedUserInfos.values());
		AllConnectedUsers allConnectedUsers = new AllConnectedUsers(connectedUserSet);
		WebSocketMessage<AllConnectedUsers> webSocketMessage = new WebSocketMessage<AllConnectedUsers>(allConnectedUsers);
		for(Session peer : peers){
    		peer.getAsyncRemote().sendObject(webSocketMessage);
        }
    }
    
	@SuppressWarnings("unchecked")
	@OnMessage
    public void handleTextMessage(Session session,@SuppressWarnings("rawtypes") WebSocketMessage webSocketMessage) {
    	
    	if (webSocketMessage.getPayload() instanceof Message) {
    		Message textMessage = (Message) webSocketMessage.getPayload();
   			webSocketMessage.setPayload(textMessage);
   		}
   		if(webSocketMessage.getPayload() instanceof ConnectedUser) { 
   			ConnectedUser newUser = (ConnectedUser) webSocketMessage.getPayload();
    		connectedUserInfos.put(session, newUser.getUsername());
    		Set<String> connectedUserSet = new HashSet<String>(connectedUserInfos.values());
    		AllConnectedUsers allConnectedUsers = new AllConnectedUsers(connectedUserSet);
    		webSocketMessage = new WebSocketMessage<AllConnectedUsers>(allConnectedUsers);
   		}
    	for(Session peer : peers){
    		peer.getAsyncRemote().sendObject(webSocketMessage);
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
}
