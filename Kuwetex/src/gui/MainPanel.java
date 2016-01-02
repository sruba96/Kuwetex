package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import network.Connection;
import network.Message;

public class MainPanel extends JPanel {
	public static final int DEFAULT_COLLUMN_SIZE = 15;
	
	private final Connection connection;
	
	private JTextArea textArea;
	private JButton connectButton, getReportButton;
	
	public MainPanel(Connection conn) {
		super (new BorderLayout());		
		connection = conn;
		
		JPanel centerPanel = new JPanel();
		textArea = new JTextArea(DEFAULT_COLLUMN_SIZE, DEFAULT_COLLUMN_SIZE * 3);
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		centerPanel.add(new JScrollPane(textArea));		
		add (centerPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel(new FlowLayout());
		connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg ="New connection: " + Calendar.getInstance().getTime().toString();
				try {
					msg = connection.connectToServer(msg);
					textArea.append(msg + "\n");
					connectButton.setEnabled(false);
				} catch (IOException e) {					
					e.printStackTrace();
				}				
			}
		});
		southPanel.add (connectButton);
		
		getReportButton = new JButton("Raport");
		getReportButton.addActionListener(new ReportButtonHandler());
		southPanel.add(getReportButton);
		
		add (southPanel, BorderLayout.SOUTH);
	}
	
	private class ReportButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {			
			String raport = connection.sendNewMessage(null, Message.GET_RAPORT);
			textArea.append(raport);
			textArea.append("\n");
		}
		
	}
}
