package kuwetexclient;

import gui.MainPanel;

import java.awt.CardLayout;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import network.Connection;

public class KuwetexClient extends JFrame {
	private static final String TITLE = "KUWETEX 1.0";
	private Connection connection = null;	
	private static JFrame applicationFrame;
	private JPanel mainPanel;
	
	public KuwetexClient() throws UnknownHostException, IOException {
		super(TITLE);
		connection = new Connection();
		setLayout(new CardLayout());
		mainPanel = new MainPanel(connection);
		add (mainPanel);	
	}
	
	public static KuwetexClient getInstance() {
		return (KuwetexClient) applicationFrame;
	}
	

	/**
	 * @param args - no needed
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				try {
					applicationFrame = new KuwetexClient();
				} catch (IOException e) {					
					e.printStackTrace();					
				}
				applicationFrame.setResizable(false);
				applicationFrame.pack();
				applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				applicationFrame.setLocationRelativeTo(null);
				applicationFrame.setVisible(true);
				System.out.println("LOADED");
			}
		});
		
	}

}
