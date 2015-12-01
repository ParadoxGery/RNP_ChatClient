package de.haw.rnp.chatclient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;

/**
 * hauptklasse des interfaces behandelt nur anzeige des Frames
 * @author Gery
 */
public class ChatClientUI {
	
	private static final int WIDTH = 500;
	private static final int HEIGHT = 300;
	
	private JFrame frame;
	
	public ChatClientUI(Container inComponent, Container outComponent) {
		frame = new JFrame();
		frame.getContentPane().add(inComponent, BorderLayout.SOUTH);
		frame.getContentPane().add(outComponent, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	public void setClosingHandler(WindowAdapter handler) {
		frame.addWindowListener(handler);
	}
	
}
