package sensors;

import other.Cat;

public interface AbstractSensor {
	/**
	 * Examines which cat is it.
	 * @return The cat attribute that was detected.
	 */
	public abstract String examine(Cat cat);
}
