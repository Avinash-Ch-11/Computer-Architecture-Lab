package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numOfStalls;
	static int numOfWrongIns;

	public Statistics(){
		numberOfCycles		= 0;
		numberOfInstructions= 0;
		numOfStalls			= 0;
		numOfWrongIns		= 0;
	}
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			Double ipc = (double) numberOfInstructions/(double) numberOfCycles;
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Instructions per cycle = " + ipc);
			writer.println("Number of stalls = " + numOfStalls);
			writer.println("Number of wrong instructions = "+ numOfWrongIns);
			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public void setNumberOfStalls(int numOfStalls) {
		Statistics.numOfStalls = numOfStalls;
	}
	public void setNumberOfWrongInstruction(int numOfWrongIns){
		Statistics.numOfWrongIns = numOfWrongIns;
	}
}
