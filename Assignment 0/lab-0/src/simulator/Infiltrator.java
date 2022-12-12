package simulator;

public class Infiltrator {
	
	public int Choice(int [] dirOptions)
	{
		if((dirOptions[0]==0 || dirOptions[1]==0 || dirOptions[2]==0) && dirOptions[3]==0) {
			return 1;
		}
		return 0;
	}

}
