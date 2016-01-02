package kuwetexclient;

import java.awt.CardLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gui.MainPanel;
import network.Connection;

public class KuwetexClient extends JFrame {
	public static final String TITLE = "KUWETEX 1.0";
	public static final String FILE_PATH = "res/data.txt";
	
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
	
	/*public static KuwetexClient getInstance() {
		return (KuwetexClient) applicationFrame;
	} */
	
	@SuppressWarnings("finally")
	public static boolean saveToFile(String text) throws IOException {
		FileWriter fw = null;
		boolean success = false;
		try {
			fw = new FileWriter(new File(FILE_PATH));
			String date = Calendar.getInstance().getTime().toString();
			date += "\n\n";
			fw.append(date);
			fw.append(text);
			success = true;
		} finally {
			if (fw != null)
				fw.close();
			return success;
		}
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
