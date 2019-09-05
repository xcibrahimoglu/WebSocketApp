package server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import model.MessageEncoder;
import model.WebSocketMessage;
import model.Message;
import model.MessageDecoder;

@ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class WebSocket {
	
	//private static final Set<Session> sessions = new HashSet<Session>();
	static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
	static Set<String> usernames = Collections.synchronizedSet(new HashSet<String>());
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());       
        peers.add(session);
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
        peers.remove(session);
    }
    
    //@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	@OnMessage
    public void handleTextMessage(Session session,@SuppressWarnings("rawtypes") WebSocketMessage webSocketMessage) {
    	for(Session peer : peers){
    		if (webSocketMessage.getPayload() instanceof Message) {
    			Message payload = (Message) webSocketMessage.getPayload();
    			webSocketMessage.setPayload(payload);
    		peer.getAsyncRemote().sendObject(webSocketMessage);
    		}
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
