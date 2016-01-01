package kuwetexserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBank {
	private static final List<Data> dataList = new ArrayList<>();
	private static final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	private class Data {
		private final String catName;
		private final String eyeColor;
		private final long timeSpent;
		private final String dateOfexcrection;
			
		Data(String name, String eyes, long time) {
			catName = name; eyeColor = eyes; timeSpent = time;
			dateOfexcrection = Calendar.getInstance().getTime().toString();
		}	
		@Override
		public String toString() {
			return catName +"; "+ eyeColor +" eyes; "+ timeSpent +"ms, on day: "+dateOfexcrection;			
		}
	}
	
	public void addNewRecord (String name, String eyes, long time) {
		rwLock.writeLock().lock();
		try {
			dataList.add (new Data(name, eyes, time));
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
}

