package processor.pipeline;
import processor.Processor;
import java.util.Scanner;
import processor.Clock;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			if(IF_EnableLatch.isIF_busy()){return;}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			if(EX_IF_Latch.getIsBranchTaken()){
				System.out.println("Branch route");
				currentPC = EX_IF_Latch.getBranchPC();
				containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC());
				EX_IF_Latch.setIsBranchTaken(false);
				containingProcessor.setNumOfBranchTaken(containingProcessor.getNumOfBranchTaken()+1);
				
			}
			// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);		
			// System.out.println("PC: "+ Integer.toString(currentPC) + " Instruction: "+ Integer.toString(newInstruction));
			containingProcessor.setNumIns(containingProcessor.getNumIns()+1);
			// containingProcessor.setNumIns(containingProcessor.getNumIns()+1);
			containingProcessor.setNumCycles(containingProcessor.getNumCycles()+1);

			// containingProcessor.getControlUnit().setOpCode(newInstruction);

			containingProcessor.getALUUnit().setControlUnit(
				containingProcessor.getControlUnit()
			);

			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), currentPC
				)
			);

			IF_EnableLatch.setIF_busy(true);
			IF_OF_Latch.setIsNop(true);

			// IF_OF_Latch.setInstruction(newInstruction);
			IF_OF_Latch.setPC(currentPC);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			
			IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);

		}
	}
	@Override
	public void handleEvent(Event e){
		if(IF_OF_Latch.isOF_busy()){
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else{
			MemoryResponseEvent event = (MemoryResponseEvent) e ;

			int newInstruction = event.getValue();
			IF_OF_Latch.setInstruction(newInstruction);
 			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);

			int currentPC= IF_OF_Latch.getPC();

			if(newInstruction == -402653184){
				IF_OF_Latch.setOF_enable(false);
				IF_EnableLatch.setIF_enable(false);
			}
		}
	}

}
