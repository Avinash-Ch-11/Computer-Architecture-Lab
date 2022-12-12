package simulator;

public class Time {
	static int totalTime = 0;
	
	public int getTotalTime() {
		return totalTime;
	}
	
	public void Account(int t) {
		totalTime = totalTime + t;
	}

}
