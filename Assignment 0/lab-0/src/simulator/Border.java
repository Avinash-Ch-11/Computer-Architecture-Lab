package simulator;

import java.util.ArrayList;

public class Border {

	public static void main(String[] args) {
		double p = Double.parseDouble(args[0]);
		int w = Integer.parseInt(args[1]);
		
		Sensor sen1 = new Sensor();
		Infiltrator inf1 = new Infiltrator();
		Time time1 = new Time();
		
		time1.Account(0);
	
		int currentDist = 0;
		while(true) {
			int [] dirOptions = sen1.switchSensor(p);
			time1.Account(1);
			int moveForward = inf1.Choice(dirOptions);
			time1.Account(9);
			if(moveForward == 1) {
				++currentDist;
				if(currentDist == w+1) {
					break;
				}
			}
			
		}
		System.out.println("p:"+ p +" w:" + w + " Time taken is " + time1.getTotalTime()+" seconds.");
	}
}
