package de.haw.rnp.chatclient.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Observable;
import java.util.concurrent.locks.ReentrantLock;

public class TCPConnection extends Observable implements Connection {
	
	private Socket socket;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	private ReentrantLock writeLock = new ReentrantLock(true);
	private ReentrantLock receiveLock = new ReentrantLock(true);
	
	@Override
	public boolean isConnected() {
		return socket != null ? !socket.isClosed() : false;
	}
	
	@Override
	public boolean connect(String srv, int port) {
		boolean success = true;
		try {
			socket = new Socket(srv, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch(IOException e) {
			success = false;
		}
		return success;
	}
	
	@Override
	public void disconnect() {
		try {
			if(socket != null)
				socket.close();
		} catch(IOException e) {
		
		}
		socket = null;
	}
	
	@Override
	public void send(String message) {
		writeLock.lock();
		writer.println(message);
		writeLock.unlock();
		System.err.println(">>"+message);
	}
	
	@Override
	public String receive() {
		receiveLock.lock();
		String message = "";
		try {
			message = reader.readLine();
		} catch(IOException e) {
			setChanged();
			notifyObservers(e);
			message = "FEHLER";
		}
		receiveLock.unlock();
		System.err.println("<<"+message);
		return message;
	}
}
