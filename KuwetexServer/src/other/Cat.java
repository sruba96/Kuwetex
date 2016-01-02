package other;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kuwetexclient.KuwetexClient;
import kuwetexserver.KuwetexServer;

public class Cat implements Runnable {
	public static final String[] NAMES = {"Dianusz", "Filemon", "Garfield"};
	public static final String[] EYES = {"blue", "red", "green"};	
	
	private final String NAME;
	private final String EYE_COLOR;
	
	public Cat(int name, int eyes) {
		NAME = NAMES[name];
		EYE_COLOR = EYES[eyes];
	}
	
	

	@Override
	public void run() {		
		while (true)
		{
			try {
				KuwetexServer.useLitterBox(this);
				Thread.sleep(KuwetexServer.random.nextInt(10_000 + KuwetexServer.ROLL));
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Error while using litter box, cat - "+NAME);
			}
		}
	}

	public String getName() {
		return NAME;
	}

	public String getEyes() {
		return EYE_COLOR;
	}

}
