package de.haw.rnp.chatclient.connection;

public interface Connection {
	
	public boolean isConnected();
	
	public void send(String message);
	
	public String receive();
	
	public abstract boolean connect(String srv, int port);
	
	public abstract void disconnect();
}
