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
		try{
			ControlUnit CU = MA_RW_Latch.getControlUnit();
			CU.setOpCode(MA_RW_Latch.getInstruction());
			if(CU.isEnd()){
				containingProcessor.setIsEnd(true);
			}
		} catch(Exception e){;}
		if(MA_RW_Latch.isRW_enable() && !MA_RW_Latch.getIsNop())
		{
			//TODO
			
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			ControlUnit CU = containingProcessor.getControlUnit();
			CU.setOpCode(MA_RW_Latch.getInstruction());
			ArithmeticLogicUnit ALU = containingProcessor.getALUUnit();

			int result = MA_RW_Latch.getALUResult();
			if(CU.isLd()){
				result = MA_RW_Latch.getLdResult();
			}
			System.out.println("Result: " + Integer.toString(result));

			int currPC = MA_RW_Latch.getPC();
			int ins = MA_RW_Latch.getInstruction();
			// System.out.println("ins: "+ins);
			String instS = Integer.toBinaryString(ins);
			
			if(ins > 0){
				instS = String.format("%32s", Integer.toBinaryString(ins)).replace(' ', '0');
			}
			// System.out.println("instS: " + instS);

			String rdStr = instS.substring(10,15);

			if(CU.isJmp()){rdStr = instS.substring(5,10);
			int rd = Integer.parseInt(rdStr, 2);}

			if(CU.isR3()){rdStr = instS.substring(15,20);}
			int rd = Integer.parseInt(rdStr, 2);

			if(CU.isWb()){
				containingProcessor.getRegisterFile().setValue(rd, result);
			}
			// if(CU.isEnd()){
			// 	containingProcessor.setIsEnd(true);
			// }

			
		}
		MA_RW_Latch.setRW_enable(false);
		// IF_EnableLatch.setIF_enable(true);
		// containingProcessor.setMA_RW_Nop(MA_RW_Latch.getIsNop());
		if(MA_RW_Latch.getIsNop()){
			MA_RW_Latch.setIsNop(false);
		}
		else {
			if(containingProcessor.getOFUnit().IF_OF_Latch.getInstruction() != -402653184){
				IF_EnableLatch.setIF_enable(true);
			}
		}
	}

}
