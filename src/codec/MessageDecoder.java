package codec;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import entity.ConnectedUser;
import entity.Message;
import entity.WebSocketMessage;

@SuppressWarnings("rawtypes")
public class MessageDecoder implements Decoder.Text<WebSocketMessage> {

	RuntimeTypeAdapterFactory<WebSocketMessage> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
			.of(WebSocketMessage.class, "type").registerSubtype(Message.class, "message")
			.registerSubtype(ConnectedUser.class, "connectedUser");

	Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
	Type type = new TypeToken<WebSocketMessage>() {
	}.getType();

	@Override
	public WebSocketMessage decode(final String Message) throws DecodeException {

		if (Message.contains("__ping__")) {
			WebSocketMessage<String> pingPongMessage = new WebSocketMessage<String>();
			pingPongMessage.setType("pingPong");
			pingPongMessage.setPayload(Message);

			return pingPongMessage;
		} else {
			
			JsonReader jsonReader = new JsonReader(new StringReader(Message));
			jsonReader.setLenient(true);
			
			@SuppressWarnings("unchecked")
			WebSocketMessage<?> webSocketMessage = new WebSocketMessage(gson.fromJson(jsonReader,type));
			return webSocketMessage;
		}
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