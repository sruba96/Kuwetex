package sensors;

import other.Cat;

public class NameSensor implements AbstractSensor {

	@Override
	public String examine(Cat cat) {
		return cat.getName();
	}
	
}
