package generic;

import java.io.*;
import java.util.Scanner;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import generic.Statistics;
import generic.EventQueue;

public class Simulator {
	
	static Processor processor;
	static boolean simulationComplete;
	static Statistics stats;
	static EventQueue eventQueue;
	
	public static EventQueue getEventQueue(){
		return eventQueue;
	}
	public static void setEventQueue(){
		Simulator.eventQueue = new EventQueue();
	}
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
		stats = new Statistics();
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		MainMemory mainMemory = processor.getMainMemory();
		try
		{
			FileInputStream inputStream = new FileInputStream(assemblyProgramFile);
			DataInputStream ins = new DataInputStream(inputStream);
			
			//PC
			int PC = ins.readInt();
			System.out.println("PC:"+PC);

			//Reading instructions from file to MainMemory
			int i=0;
			while(ins.available() > 0)
			{
				int dummy = ins.readInt();
				processor.getMainMemory().setWord(i, dummy);
				System.out.println(dummy);
				i++;
			}
			ins.close();
			processor.setMainMemory(mainMemory);
			System.out.println(".out file read completed");
			
			//2. set PC to the address of the first instruction in the main
			processor.getRegisterFile().setProgramCounter(PC);
			//System.out.println(PC);

			// 3. set the following registers: x0 = 0 | x1 = 65535 | x2 = 65535
			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);

			System.out.println(mainMemory.getContentsAsString(0,30));

		}
		catch(FileNotFoundException fe){System.out.println("File not found" + fe);}
		catch (IOException ex){System.out.println("IOException: " + ex);}
	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			// System.out.println("--------RW--------");
			processor.getRWUnit().performRW();
			// System.out.println("--------MA--------");
			processor.getMAUnit().performMA();
			// System.out.println("--------EX--------");
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			// System.out.println("--------OF--------");
			processor.getOFUnit().performOF();
			// System.out.println("--------IF--------");
			processor.getIFUnit().performIF();
			
			Clock.incrementClock();
			processor.setNumCycles(processor.getNumCycles() + 1);


			System.out.println("___________________________________");
			System.out.println("\n\n");
			setSimulationComplete(processor.getIsEnd());
		}
		
		// TODO
		// set statistics
		stats.setNumberOfInstructions(processor.getNumIns());
		stats.setNumberOfCycles(processor.getNumCycles());
		stats.setNumberOfStalls(processor.getNumOfStalls());
		stats.setNumberOfWrongInstruction(processor.getNumOfBranchTaken() *2);
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
