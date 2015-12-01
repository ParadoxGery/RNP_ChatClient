package de.haw.rnp.chatclient.input;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputComponentUI {
	
	private JPanel ui;
	
	private JTextField inputField;
	private JButton sendButton;
	
	public InputComponentUI() {
		ui = new JPanel(new BorderLayout());
		inputField = new JTextField();
		sendButton = new JButton("Send");
		ui.add(inputField,BorderLayout.CENTER);
		ui.add(sendButton,BorderLayout.EAST);
		active(false);
	}
	
	public void active(boolean active) {
		inputField.setEditable(active);
		sendButton.setEnabled(active);
		inputField.requestFocus();
	}
	
	public void setListener(ActionListener l) {
		inputField.addActionListener(l);
		sendButton.addActionListener(l);
	}
	
	public void clear() {
		inputField.setText("");
	}
	
	public JPanel getUi() {
		return ui;
	}

	public String getMessage() {
		return inputField.getText();
	}
}
