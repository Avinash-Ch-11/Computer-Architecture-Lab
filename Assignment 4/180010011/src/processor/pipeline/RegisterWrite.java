package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import java.util.Scanner;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			ControlUnit CU = containingProcessor.getControlUnit();
			ArithmeticLogicUnit ALU = containingProcessor.getALUUnit();

			int result = ALU.getALUResult();
			if(CU.isLd()){
				result = MA_RW_Latch.getLdResult();
			}
			System.out.println("Result: " + Integer.toString(result));

			int currPC = containingProcessor.getRegisterFile().getProgramCounter();
			int ins = containingProcessor.getMainMemory().getWord(currPC-1);
			// System.out.println("ins: "+ins);
			String instS = Integer.toBinaryString(ins);
			
			if(ins > 0){
				instS = String.format("%32s", Integer.toBinaryString(ins)).replace(' ', '0');
			}
			// System.out.println("instS: " + instS);

			String rdStr = instS.substring(10,15);

			if(CU.isJmp()){rdStr = instS.substring(5,10);}

			if(CU.isR3()){rdStr = instS.substring(15,20);}
			int rd = Integer.parseInt(rdStr, 2);

			if(CU.isWb()){
				containingProcessor.getRegisterFile().setValue(rd, result);
			}
			if(CU.isEnd()){
				containingProcessor.setIsEnd(true);
			}

			
		}
		MA_RW_Latch.setRW_enable(false);
		IF_EnableLatch.setIF_enable(true);
	}

}
