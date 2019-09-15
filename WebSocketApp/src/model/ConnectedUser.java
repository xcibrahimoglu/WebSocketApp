package model;

@SuppressWarnings("rawtypes")
public class ConnectedUser extends WebSocketMessage{

	public static final String type = "connectedUser";
	private String username;
	

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

}
