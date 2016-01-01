package other;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kuwetexclient.KuwetexClient;
import kuwetexserver.KuwetexServer;

public class Cat implements Runnable {
	public static final String[] NAMES = {"Dianusz", "Filemon", "Garfield"};
	public static final String[] EYES = {"blue", "red", "green"};
	
	private static final Random random = new Random();
	private static final int ROLL = 5_000; // for random time generation
	
	private static final Lock lock = new ReentrantLock();
	
	private final String NAME;
	private final String EYE_COLOR;
	
	public Cat(int name, int eyes) {
		NAME = NAMES[name];
		EYE_COLOR = EYES[eyes];
	}
	
	public void useLitterBox() throws InterruptedException {
		int time = random.nextInt(ROLL);
		long t0, t1;
		lock.lock(); // only one cat can use litter box
		try {
			t0 = System.currentTimeMillis();
			System.out.println("Cat "+NAME+" has entered.");
			Thread.sleep(time);
			t1 = System.currentTimeMillis();
		} finally {
			lock.unlock();
		}
		
		// save data
		System.out.println("Cat "+NAME+" has exited.");
		updateData( (t1-t0) ); // total time spent in litter box (t1-t0)
	}

	private void updateData(long timeSpent) {		
		//TODO dodac mechanizm wazenia kota w kuwecie
		
		KuwetexServer.dataBank.addNewRecord(NAME, EYE_COLOR, timeSpent);
	}

	@Override
	public void run() {		
		while (true)
		{
			try {
				useLitterBox();
				Thread.sleep(random.nextInt(10_000 + ROLL));
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Error while using litter box, cat - "+NAME);
			}
		}
	}

}
