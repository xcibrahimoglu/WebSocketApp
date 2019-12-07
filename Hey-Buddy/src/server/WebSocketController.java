package server;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import codec.MessageDecoder;
import codec.MessageEncoder;
import model.ConnectedUser;
import model.DatabaseOperations;
import model.DisconnectedUser;
import model.Message;
import model.WebSocketMessage;

@ServerEndpoint(value = "/ws", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class WebSocketController {

	static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	DatabaseOperations database = DatabaseOperations.getInstance(); // connect DB

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen::" + session.getId());
		clients.add(session);
		String[] keys = {"sender","receiver"};
		List<Message<?>> messages = database.findDocument(keys, session.getUserPrincipal().getName());
		for(Message<?> message : messages) {
			WebSocketMessage<Message<?>> webSocketMessage =new WebSocketMessage<Message<?>>(message);
			session.getAsyncRemote().sendObject(webSocketMessage);
		}
		
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose::" + session.getId());
		clients.remove(session);
		sendUserStatusToAllClients(session.getUserPrincipal().getName(), false);
	}

	@OnMessage(maxMessageSize = 40024000)
	public void handleMessage(Session session, @SuppressWarnings("rawtypes") WebSocketMessage webSocketMessage) {

		if (webSocketMessage.getPayload() instanceof Message) {

			Message<?> message = (Message<?>) webSocketMessage.getPayload();

			database.createDocument(message);

			for (Session client : clients) {
				if (client.getUserPrincipal().getName().equalsIgnoreCase(message.getReceiver())
						|| client.getUserPrincipal().getName().equalsIgnoreCase(message.getSender()))
					client.getAsyncRemote().sendObject(webSocketMessage);
			}
		}
		if (webSocketMessage.getPayload() instanceof ConnectedUser) {
			for (Session client : clients) {
				sendUserStatusToAllClients(client.getUserPrincipal().getName(), true);
			}
		}
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("onError::" + t.getMessage());
	}

	public void sendUserStatusToAllClients(String user, Boolean status) {
		if (status == true) { // it means "Online"
			ConnectedUser connectedUser = new ConnectedUser(user);
			WebSocketMessage<ConnectedUser> webSocketMessage = new WebSocketMessage<ConnectedUser>(connectedUser);

			for (Session client : clients) {
				client.getAsyncRemote().sendObject(webSocketMessage);
			}
		} else if (status == false) { // it means "Offline"
			DisconnectedUser disconnectedUser = new DisconnectedUser(user);
			WebSocketMessage<DisconnectedUser> webSocketMessage = new WebSocketMessage<DisconnectedUser>(
					disconnectedUser);

			for (Session client : clients) {
				client.getAsyncRemote().sendObject(webSocketMessage);
			}
		}

	}
}
