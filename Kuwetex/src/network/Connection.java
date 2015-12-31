package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
	private static final String ADRESS = "localhost";
	private static final int PORT = 4444;
	
	private Message message;
	private boolean isConnected = false;
	
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	public Connection() {		
	}
	
	public void connectToServer(String msg) throws UnknownHostException, IOException {
		if (isConnected) return;
		
		socket = new Socket(ADRESS, PORT);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		
		sendNewMessage(msg, Message.LOG_ME_IN);
		isConnected = true;
	}
	
	public void sendNewMessage (String text, int header) {
		message = new Message(text, header);				
	}
	
	@SuppressWarnings("finally")
	public boolean closeConnection() {
		boolean tof = false;
		message = new Message("client has logged out", Message.LOG_OUT);		
		try {
			out.writeObject(message);
			out.flush();
			
			if (socket != null)
				socket.close();			
			tof = true;			
			isConnected = false;
		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			return tof;
		}
	}
}
