package sensors;

import other.Cat;

public class EyeSensor implements AbstractSensor {

	@Override
	public String examine(Cat cat) {
		return cat.getEyes();
	}

}
