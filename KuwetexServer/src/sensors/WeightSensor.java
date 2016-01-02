package sensors;

import kuwetexserver.KuwetexServer;
import other.Cat;

public class WeightSensor implements AbstractSensor {

	@Override
	public String examine(Cat cat) {
		Integer weight = (KuwetexServer.random.nextInt(5)+2);
		return weight.toString() + " kg";
	}

}
