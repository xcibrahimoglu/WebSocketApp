package model;

import java.util.Set;

@SuppressWarnings("rawtypes")
public class AllOnlineUsers extends WebSocketMessage{

	public static final String type = "allonlineusers";
	private Set<String> usernames;

	public Set<String> getUsernames() {
		return usernames;
	}
	

	public void setUsernames(Set<String> usernames) {
		this.usernames = usernames;
	}

}
