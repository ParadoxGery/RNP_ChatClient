package de.haw.rnp.chatclient.output;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class OutputComponentUI {
	
	private JPanel ui;
	private JPanel userList;
	private JScrollPane userScroll;
	private JTextArea chatView;
	private JScrollPane chatScroll;
	
	public OutputComponentUI() {
		ui = new JPanel(new BorderLayout());
		
		chatView = new JTextArea();
		chatView.setEditable(false);
		chatView.setLineWrap(true);
		chatView.setWrapStyleWord(true);
		((DefaultCaret)chatView.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatScroll = new JScrollPane(chatView);
		chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroll.setAutoscrolls(true);
		ui.add(chatScroll, BorderLayout.CENTER);
		
		userList = new JPanel();
		userList.setPreferredSize(new Dimension(100,100));
		userList.setLayout(new BoxLayout(userList, BoxLayout.Y_AXIS));
		userScroll = new JScrollPane(userList);
		ui.add(userScroll, BorderLayout.EAST);
	}
	
	public void appendMessage(String message) {
		chatView.append("\r" + "\n" + message);
	}
	
	public void updateUsers(String users) {
		userList.removeAll();
		String[] userArr = users.split("\r\n");
		for(String s : userArr){
			userList.add(new JLabel(s));
		}
		userList.validate();
		userList.repaint();
	}
	
	public JPanel getUi() {
		return ui;
	}
}
