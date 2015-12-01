package de.haw.rnp.chatclient.output;

import javax.swing.JPanel;
import de.haw.rnp.chatclient.connection.Connection;
import de.haw.rnp.chatclient.protokoll.Protokoll;

public class OutputComponent {
	
	private OutputComponentUI ui;
	
	private Connection connection;
	
	private Thread listenThread;
	
	private Thread askingThread;
	
	public OutputComponent() {
		ui = new OutputComponentUI();
		
		listenThread = new Thread() {
			@Override
			public void run() {
				connection.send(Protokoll.ASK_USERS);
				while(!Thread.currentThread().isInterrupted()) {
					String message = connection.receive();
					if(message != null) {
						if(message.startsWith(Protokoll.USER_PREFIX)) {
							String users = message.substring(Protokoll.USER_PREFIX.length(), message.length());
							ui.updateUsers(users.replaceAll(Protokoll.USER_SPLIT, "\r" + "\n"));
						} else if(message.startsWith(Protokoll.MSGPREFIX)) {
							ui.appendMessage(message.substring(Protokoll.MSGPREFIX.length(), message.length()));
						} else if(message.startsWith(Protokoll.USERSUPDATE) && !Protokoll.USERASK) {
							new Thread() {
								@Override
								public void run() {
									connection.send(Protokoll.ASK_USERS);
								}
							}.start();
						}
					}
				}
			}
		};
		
		askingThread = new Thread() {
			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()) {
					connection.send(Protokoll.ASK_USERS);
					try {
						sleep(Protokoll.ASKTHREAD_SLEEP);
					} catch(InterruptedException e) {
						interrupt();
					}
				}
			}
		};
	}
	
	public JPanel getUi() {
		return ui.getUi();
	}
	
	public void start() {
		if(!listenThread.isAlive()) {
			listenThread.start();
		}
		if(Protokoll.USERASK) {
			if(!askingThread.isAlive()) {
				askingThread.start();
			}
		}
	}
	
	public void stop() {
		if(listenThread.isAlive()) {
			listenThread.interrupt();
		}
		if(Protokoll.USERASK) {
			if(askingThread.isAlive()) {
				askingThread.interrupt();
			}
		}
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
