package model;

import java.lang.reflect.Type;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("rawtypes")
public class MessageDecoder implements Decoder.Text<WebSocketMessage> {
	
	RuntimeTypeAdapterFactory<WebSocketMessage> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
		    .of(WebSocketMessage.class, "type")
		    .registerSubtype(Message.class, "message")
		    .registerSubtype(AllOnlineUsers.class, "allonlineusers");
	
	Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
	Type type = new TypeToken<WebSocketMessage>(){}.getType();

    @Override
    public WebSocketMessage decode(final String Message) throws DecodeException {
    	WebSocketMessage<?> webSocketMessage = new WebSocketMessage();
    	webSocketMessage.setPayload(gson.fromJson(Message,type));        
        return webSocketMessage;
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean willDecode(String arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
