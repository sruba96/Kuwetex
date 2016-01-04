package other;

import kuwetexserver.KuwetexServer;

public class Cat implements Runnable {
	public static final String[] NAMES = {"Dianusz", "Filemon", "Garfield"};
	public static final String[] EYES = {"blue", "red", "green"};
	
	// gender types
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	
	private final String NAME;
	private final String EYE_COLOR;
	private final int GENDER;
	
	
	public Cat(int name, int eyes) {
		NAME = NAMES[name];
		EYE_COLOR = EYES[eyes];
		GENDER = KuwetexServer.random.nextInt(2);
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
	public int getGender() {
		return this.GENDER;
	}

}
