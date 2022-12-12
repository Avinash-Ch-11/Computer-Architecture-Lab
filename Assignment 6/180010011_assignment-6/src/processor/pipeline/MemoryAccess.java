package processor.pipeline;

import java.util.Scanner;
import processor.Processor;
import processor.Clock;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;


public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		// containingProcessor.setEX_MA_Nop(EX_MA_Latch.getIsNop());
		if(EX_MA_Latch.isMA_busy()){return;}

		if(EX_MA_Latch.getInstruction()==-402653184){
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			MA_RW_Latch.setControlUnit(EX_MA_Latch.getControlUnit());
			MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
		}

		if(EX_MA_Latch.getIsNop()){
			EX_MA_Latch.setIsNop(false);
			MA_RW_Latch.setIsNop(true);
			System.out.println("MA: NOP");
		}
		if(EX_MA_Latch.isMA_enable()){
			ControlUnit CU = EX_MA_Latch.getControlUnit();
			CU.setOpCode(EX_MA_Latch.getInstruction());
			System.out.println(EX_MA_Latch.getInstruction());
			if(CU.isLd()){
				int loaddResult;
				// loaddResult =containingProcessor.getMainMemory().getWord(EX_MA_Latch.getALUResult());
				// MA_RW_Latch.setLdResult(loaddResult);
				// System.out.println("ALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
				// System.out.println("LoadResult: " + Integer.toString(loaddResult));
				// Simulator.getEventQueue().addEvent(
				// 	new MemoryReadEvent(
				// 		Clock.getCurrentTime() + Configuration.mainMemoryLatency,
				// 		this,
				// 		containingProcessor.getMainMemory(),
				// 		EX_MA_Latch.getALUResult()
				// 	)
				// );
				containingProcessor.getL1d_cache().cacheRead(EX_MA_Latch.getALUResult(), this);
				containingProcessor.getOFUnit().OF_EX_Latch.setEX_busy(true);
				MA_RW_Latch.setIsNop(true);

				EX_MA_Latch.setMA_busy(true);
				return;
			}
			else if(CU.isSt()){
				int loc = EX_MA_Latch.getALUResult();
				int data = EX_MA_Latch.getOp2();
				// System.out.println("Loc: "+ Integer.toString(loc));
				// System.out.println("data: " + Integer.toString(data));
				// containingProcessor.getMainMemory().setWord(loc,data);

				// Simulator.getEventQueue().addEvent(
				// 	new MemoryWriteEvent(
				// 		Clock.getCurrentTime() + Configuration.mainMemoryLatency,
				// 		this,
				// 		containingProcessor.getMainMemory(),
				// 		loc,data
				// 	)
				// );
				containingProcessor.getL1d_cache().cacheWrite(loc, data, this);
				containingProcessor.getOFUnit().OF_EX_Latch.setEX_busy(true);
				MA_RW_Latch.setIsNop(true);
				EX_MA_Latch.setMA_busy(true);
				return;
			}

			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			MA_RW_Latch.setALUResult(EX_MA_Latch.getALUResult());
			MA_RW_Latch.setControlUnit(CU);

			// if(EX_MA_Latch.isMA_enable()){MA_RW_Latch.setRW_enable(true);}
			// else{MA_RW_Latch.setRW_enable(false);}
			// EX_MA_Latch.setMA_enable(false);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);

		}
	}
	@Override
	public void handleEvent(Event e) {
		ControlUnit CU = EX_MA_Latch.getControlUnit();
		CU.setOpCode(EX_MA_Latch.getInstruction());
		EX_MA_Latch.setMA_busy(false);
		containingProcessor.getOFUnit().OF_EX_Latch.setEX_busy(false);
		if(e.getEventType() == EventType.MemoryResponse){
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			int ldResult = event.getValue();
			if(ldResult!=-1){MA_RW_Latch.setLdResult(ldResult);}
		}

		MA_RW_Latch.setPC(EX_MA_Latch.getPC());
		MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
		MA_RW_Latch.setALUResult(EX_MA_Latch.getALUResult());

		MA_RW_Latch.setControlUnit(CU);

		EX_MA_Latch.setMA_enable(false);
		MA_RW_Latch.setRW_enable(true);
	}
}
