package kuwetexserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBank {
	private static final List<Data> dataList = new ArrayList<>();
	private static final List<IllnessHistory> illnessHistory = new ArrayList<>();
	//Malina was here
	// for ill cats
	private static final String[] PRESCRIPTIONS = {"Go to doctor.", "Dig the grave.",
			"Do not do anything, cat will recover soon.", "Cat needs to stress out. Give him/her Prozac."}; 
	
	private static final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	private class Data {
		private final String catName;
		private final String eyeColor;
		private final long timeSpent;
		private final String dateOfexcrection;
		private final String weight;
		private final String health;
			
		Data(String name, String eyes, String weight, long time, String health) {
			catName = name; eyeColor = eyes; timeSpent = time; this.weight = weight; this.health = health;
			dateOfexcrection = Calendar.getInstance().getTime().toString();
		}	
		@Override
		public String toString() {
			return "* "+catName +"; "+ eyeColor+" eyes; "+"weight: "+weight+
					";  "+timeSpent +"ms, on day: "+dateOfexcrection + ";\n" + health;			
		}
	}
	
	private class IllnessHistory {
		String catName;
		String date;
		String recommendation;
		
		public IllnessHistory(String cat, String date) {
			catName = cat; this.date = date;
			recommendation = PRESCRIPTIONS[KuwetexServer.random.nextInt(PRESCRIPTIONS.length)];
		}
		
		@Override
		public String toString() {			
			return catName + " was ill on " + date +". Recommendation:\n" + recommendation;
		}
	}
	
	public void addNewRecord (String name, String eyes, String weight ,long time, String health) {
		rwLock.writeLock().lock();
		try {
			dataList.add (new Data(name, eyes, weight, time, health));
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	public void addNewIllCat(String catName, String date) {
		rwLock.writeLock().lock();
		try {
			illnessHistory.add(new IllnessHistory(catName, date));
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		rwLock.readLock().lock();
		try {
			for (Data d : dataList) {
				builder.append(d.toString());
				builder.append("\n");
			}
		} finally {
			rwLock.readLock().unlock();
		}
		return builder.toString();
	}
	
	public String getPrescriptions() {
		StringBuilder builder = new StringBuilder();
		rwLock.readLock().lock();
		try {
			for (IllnessHistory illness : illnessHistory) {
				builder.append("* ");
				builder.append(illness.toString());
				builder.append("\n");
			}
		} finally {
			rwLock.readLock().unlock();
		}
		return builder.toString();
	}
}

