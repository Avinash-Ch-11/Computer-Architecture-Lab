package processor.pipeline;

import java.util.Scanner;
import processor.Processor;

public class MemoryAccess {
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
		//TODO
		ControlUnit CU = containingProcessor.getControlUnit();
		if(CU.isLd()){
			int loaddResult;
			loaddResult =containingProcessor.getMainMemory().getWord(EX_MA_Latch.getALUResult());
			MA_RW_Latch.setLdResult(loaddResult);
			System.out.println("ALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
			System.out.println("LoadResult: " + Integer.toString(loaddResult));
		}
		else if(CU.isSt()){
			int loc = EX_MA_Latch.getALUResult();
			int data = EX_MA_Latch.getOp2();
			System.out.println("Loc: "+ Integer.toString(loc));
			System.out.println("data: " + Integer.toString(data));
			containingProcessor.getMainMemory().setWord(loc,data);
		}
		if(EX_MA_Latch.isMA_enable()){MA_RW_Latch.setRW_enable(true);}
		else{MA_RW_Latch.setRW_enable(false);}
		EX_MA_Latch.setMA_enable(false);
	}

}
