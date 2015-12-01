package de.haw.rnp.chatclient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import de.haw.rnp.chatclient.connection.Connection;
import de.haw.rnp.chatclient.connection.TCPConnection;
import de.haw.rnp.chatclient.input.InputComponent;
import de.haw.rnp.chatclient.output.OutputComponent;
import de.haw.rnp.chatclient.protokoll.Protokoll;

/**
 * logik klasse des chat clients behandelt verbindungs abbruch und neuverbindung, authentifizierung
 * @author Gery
 */
public class ChatClient {
	
	/**
	 * container des jframes
	 */
	private ChatClientUI ui;
	
	private InputComponent input;
	private OutputComponent output;
	
	private TCPConnection connection;
	
	private String username = "";
	
	public ChatClient() {
		input = new InputComponent();
		output = new OutputComponent();
		ui = new ChatClientUI(input.getUi(), output.getUi());
		ui.setClosingHandler(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				enable(false);
				connection.send(Protokoll.QUIT);
				connection.disconnect();
				System.exit(0);
			}
		});
		
		init();
	}
	
	private void enable(boolean enabled) {
		input.active(enabled);
		if(enabled) {
			output.start();
		} else {
			output.stop();
		}
	}
	
	private void connect() {
		JTextField name = new JTextField(username);
		JTextField server = new JTextField();
		JSpinner port = new JSpinner();
		Object[] message = {"Username", name, "Server", server, "Port", port};
		
		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		
		while(connection == null || !connection.isConnected()) {
			pane.createDialog(null, "Verbindung").setVisible(true);
			
			if((int)pane.getValue() == 0) {
				username = name.getText();
				connection = new TCPConnection();
				connection.addObserver(new Observer() {
					
					@Override
					public void update(Observable o, Object arg) {
						if(o instanceof Connection) {
							if(arg instanceof IOException) {
								connectionLostPane();
								connection.disconnect();
								enable(false);
								init();
							}
						}
					}
				});
				
				if(server.getText() != null && server.getText().length() > 0 && port.getValue() != null
						&& (int)port.getValue() > 0) {
					if(connection.connect(server.getText(), (int)port.getValue())) {
						input.setConnection(connection);
						output.setConnection(connection);
					} else {
						connectionLostPane();
					}
				}
			} else {
				System.exit(1);
			}
		}
	}
	
	private boolean login() {
		if(!Protokoll.SERVERSTARTS) {
			connection.send(Protokoll.AUTH_START_CLIENT);
		}
		String serverMessage = connection.receive();
		if(serverMessage.equals(Protokoll.AUTH_START)) {
			connection.send(Protokoll.AUTH_USERNAME + username);
		}
		serverMessage = connection.receive();
		if(serverMessage.equals(Protokoll.AUTH_ACC)) {
			ui.setTitle("verbunden als: " + username);
			return true;
		} else if(serverMessage.startsWith(Protokoll.AUTH_FAILED)) {
			JOptionPane.showMessageDialog(null, "Benutzername bereits vorhanden", "Warning",
					JOptionPane.WARNING_MESSAGE);
			JTextField name = new JTextField(username);
			Object[] message = {"Username", name};
			JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			pane.createDialog(null, "Neuer Benutzername").setVisible(true);
			if(pane.getValue()!=null && (int)pane.getValue() == 0) {
				username = name.getText();
			} else {
				System.exit(1);
			}
			return false;
		}
		JOptionPane.showMessageDialog(null, "Fehler im protokoll: " + serverMessage, "Warning",
				JOptionPane.WARNING_MESSAGE);
		return false;
	}
	
	private void init() {
		connect();
		if(login()) {
			enable(true);
		} else {
			init();
		}
	}
	
	private void connectionLostPane() {
		JOptionPane.showMessageDialog(null, "Keine Verbindung", "FEHLER", JOptionPane.ERROR_MESSAGE);
		ui.setTitle("nicht verbunden");
	}
}
