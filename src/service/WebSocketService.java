package service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import codec.MessageDecoder;
import codec.MessageEncoder;
import entity.ConnectedUser;
import repository.MessageRepository;
import entity.DisconnectedUser;
import entity.Message;
import entity.WebSocketMessage;

@ServerEndpoint(value = "/ws", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class WebSocketService {

	static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	MessageRepository database = new MessageRepository(); // connect DB

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen::" + session.getId());
		clients.add(session);
		String[] keys = { "sender", "receiver" };
		List<Message<?>> messages = database.findDocument(keys, session.getUserPrincipal().getName());

		messages.forEach(message -> {
			WebSocketMessage<Message<?>> webSocketMessage = new WebSocketMessage<Message<?>>(message);
			session.getAsyncRemote().sendObject(webSocketMessage);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose::" + session.getId());
		clients.remove(session);
		sendUserStatusToAllClients(session.getUserPrincipal().getName(), false);
	}

	@SuppressWarnings("unchecked")
	@OnMessage(maxMessageSize = 40024000)
	public void handleMessage(Session session, @SuppressWarnings("rawtypes") WebSocketMessage webSocketMessage) {
		
		if(webSocketMessage.getPayload() instanceof String) {
			webSocketMessage.setPayload("__pong__");
			clients.forEach(client -> {
				if(client == session) {
					client.getAsyncRemote().sendObject(webSocketMessage);
				}
			});
		}

		if (webSocketMessage.getPayload() instanceof Message) {

			Message<?> message = (Message<?>) webSocketMessage.getPayload();

			database.createDocument(message);

			clients.forEach(client -> {
				if (client.getUserPrincipal().getName().equalsIgnoreCase(message.getReceiver())
						|| client.getUserPrincipal().getName().equalsIgnoreCase(message.getSender())) {
					client.getAsyncRemote().sendObject(webSocketMessage);
				}
			});

		}
		if (webSocketMessage.getPayload() instanceof ConnectedUser) {
			clients.forEach(client -> {
				sendUserStatusToAllClients(client.getUserPrincipal().getName(), true); 
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

	@OnError
	public void onError(Throwable t) {
		System.out.println("onError::" + t.getMessage());
		t.printStackTrace();
	}

	public void sendUserStatusToAllClients(String user, Boolean status) {
		if (status == true) { // it means "Online"
			ConnectedUser connectedUser = new ConnectedUser(user);
			WebSocketMessage<ConnectedUser> webSocketMessage = new WebSocketMessage<ConnectedUser>(connectedUser);

			System.out.println("connected user: " + user);
			clients.forEach(client -> client.getAsyncRemote().sendObject(webSocketMessage));

		} else if (status == false) { // it means "Offline"
			DisconnectedUser disconnectedUser = new DisconnectedUser(user);
			WebSocketMessage<DisconnectedUser> webSocketMessage = new WebSocketMessage<DisconnectedUser>(
					disconnectedUser);
			System.out.println("disconnected user: " + user);
			clients.forEach(client -> client.getAsyncRemote().sendObject(webSocketMessage));

		}

	}
}
