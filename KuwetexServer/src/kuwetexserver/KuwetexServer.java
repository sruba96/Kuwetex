package kuwetexserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import other.Cat;
import sensors.*;

public class KuwetexServer {
	private static final int PORT = 4444;
	private static int idCounter = 0;
	private static int litterBoxDirtiness = 0;
	private static final int MAX_DIRTINESS_LEVEL = 7;
	public static final Random random = new Random();
	public static final int ROLL = 5_000; // for random time generation
	//public static final String Separator = ";";
	
	private Map<Integer, Socket> clientMap;
	private boolean isWorking = false;
	private ServerSocket serverSocket;
	
	private static final DataBank dataBank = new DataBank();
	private static final Lock lock = new ReentrantLock();
	
	// sensors
	private static final AbstractSensor eyeSensor = new EyeSensor(),
								weightMachine = new WeightSensor(),
								nameRecognization = new NameSensor();
	
	// constructor
	public KuwetexServer() throws IOException {
		clientMap = new HashMap<>();
		serverSocket = new ServerSocket(PORT);
	}
	
	public void startServer() {
		isWorking = true;		
		// clearness monitor thread
		new Thread(new CleaningSystemRunnable()).start();
		
		// creating virtual cats
		for (int i=0; i<Cat.NAMES.length; i++) {
			Thread t = new Thread(new Cat(i, i));
			t.start();
		}
		System.out.println("server started");		
		while (isWorking)
		{
			try {
				Socket socket = serverSocket.accept();		
				// new client thread
				Thread t = new Thread(new ClientWorker(socket, idCounter++, clientMap));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("server closed");
	}
	
	public static void useLitterBox(Cat cat) throws InterruptedException {
		String eyes = eyeSensor.examine(cat);
		String weight = weightMachine.examine(cat);
		String name = nameRecognization.examine(cat);
		
		int time = random.nextInt(ROLL);
		long t0, t1;
		lock.lock(); // only one cat can use litter box
		try {
			t0 = System.currentTimeMillis();
			System.out.println("Cat "+name+" has entered.");
			Thread.sleep(time);
			t1 = System.currentTimeMillis();
			litterBoxDirtiness += random.nextInt(2) + 1; // 1=small poop, 2=big poop
		} finally {
			lock.unlock();
		}
		
		// save data
		System.out.println("Cat "+name+" has exited.");
		updateData((t1-t0), name, eyes, weight); // total time spent in litter box (t1-t0)
	}


	private static void updateData(long timeSpent, String name, String eyes, String weight) {	
		System.out.println("Updating history");
		dataBank.addNewRecord(name, eyes, weight, timeSpent);
		//TODO health status check
	}
	
	public static String getRaport() {
		return dataBank.toString();
	}
	
	private class CleaningSystemRunnable implements Runnable {
		@Override
		public void run() {
			while (true)
			{
				try {
					clearLitterBox(false); // not forced
					Thread.sleep(8_000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void clearLitterBox(boolean forcedClean) throws InterruptedException {
		lock.lock(); // There can not be any cat inside during cleaning process
		try {
			if ( (litterBoxDirtiness >= MAX_DIRTINESS_LEVEL) || forcedClean) {
				if (forcedClean) {
					System.out.println("Cleaning is forced by user.");
				}				
				System.out.println("Cleaning process is running. Dirtiness level: "+litterBoxDirtiness);
				litterBoxDirtiness = 0;
				Thread.sleep(random.nextInt(ROLL)); // cleaning time
			} else {
				System.out.println("CLEANING SYSTEM: Dirtness at normal level. Not cleaning.");
			}
		} finally {
			lock.unlock();			
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
