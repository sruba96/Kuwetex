package kuwetexserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class KuwetexServer {
	private static final int PORT = 4444;
	private static int idCounter = 0;
	
	private Map<Integer, Socket> clientMap;
	private boolean isWorking = false;
	private ServerSocket serverSocket;
	
	public KuwetexServer() throws IOException {
		clientMap = new HashMap<>();
		serverSocket = new ServerSocket(PORT);
	}
	
	public void startServer() {
		isWorking = true;
		while (isWorking)
		{
			try {
				Socket socket = serverSocket.accept();				
				Thread t = new Thread(new ClientWorker(socket, idCounter++, clientMap));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param args - no needed
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new KuwetexServer().startServer();
	}

}
