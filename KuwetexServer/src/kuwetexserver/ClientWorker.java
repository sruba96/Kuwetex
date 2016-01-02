package kuwetexserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

import network.Message;

class ClientWorker implements Runnable {
	private final Socket socket;	
	private final ObjectInputStream in;
	private final ObjectOutputStream out;
	private final int ID;	
	private final Map<Integer, Socket> clientMap;
	private static int online = 0;
		
	public ClientWorker(Socket s, int id, Map<Integer, Socket> map) throws IOException {
		socket = s;
		ID = id;
		clientMap = map;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		online++;
	}
	@Override
	public void run() {
		while (socket.isConnected()) 
		{			
				Message message;
				try {
					message = (Message) in.readObject();
					response(message);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					System.out.println("Error, user #" + ID);
					logOutUser();
					break;
				}							
		}
	}
	private void response(Message message) {
		final int header = message.getHEADER();
		System.out.println("Message from client #" + ID + ": " + message.getMessage());
		switch (header) {			
		case Message.LOG_ME_IN: {
			System.out.println("Online now: "+online);
			clientMap.put(ID, socket); // add me to the map
			message = new Message("You are connected.", Message.LOG_ME_IN);
			sendMessage(message);
			break;
		}
		case Message.LOG_OUT: {
			logOutUser();
			break;
		}
		case Message.GET_RAPORT: {
			String raport = KuwetexServer.dataBank.toString();
			message = new Message(raport, Message.GET_RAPORT);
			sendMessage(message);
			break;
		}
		default: {
			System.out.println("Error, no such case.");
		}
		} // end of switch
	}
	
	private void logOutUser() {
		if (!socket.isClosed())
			try {
				System.out.println("Logging out user #" + ID);
				socket.close();
			} catch (IOException e1) {
			}
		clientMap.remove(ID);
		online--;
		System.out.println("User #"+ID+" has disconnected. Online now: " + online);
	}
	
	private void sendMessage (Message message) {
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error while sending message back to the client #"+ID);
		}
	}

}
