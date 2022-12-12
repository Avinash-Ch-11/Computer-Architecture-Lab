package simulator;

public class Sensor {
	
	public int [] switchSensor(double p) {
		int [] dirOptions = {-1, -1, -1, -1};//[Front, Front left, Front right, Current cell]
		double threshold = p;
		
		for(int i=0; i<4; i++) {
			double randomNumber = Math.random();
			if(threshold < randomNumber) {
				dirOptions[i] = 0;
			}
			else {
				dirOptions[i] = 1;
			}
		}
		
		return dirOptions;
	}
}
