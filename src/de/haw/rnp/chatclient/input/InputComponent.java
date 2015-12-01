package de.haw.rnp.chatclient.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import de.haw.rnp.chatclient.connection.Connection;
import de.haw.rnp.chatclient.protokoll.Protokoll;

public class InputComponent {
	
	private InputComponentUI ui;
	
	private Connection connection;
	
	private boolean serviceRequired;
	
	public InputComponent() {
		serviceRequired = false;
		ui = new InputComponentUI();
		ui.setListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				send(Protokoll.MSGPREFIX + " " + ui.getMessage());
				ui.clear();
			}
			
		});
	}
	
	public void active(boolean active) {
		serviceRequired = active;
		ui.active(active);
	}
	
	public JPanel getUi() {
		return ui.getUi();
	}
	
	private void send(String message) {
		if(connection.isConnected() && serviceRequired) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					connection.send(message);
				}
				
			}).start();
		}
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
