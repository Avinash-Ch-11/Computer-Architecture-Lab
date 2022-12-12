package processor.pipeline;

import java.util.Scanner;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		//TODO
		// Get cu and ALU for flags and arithmetic
		ControlUnit CU = containingProcessor.getControlUnit();
		ArithmeticLogicUnit ALU = containingProcessor.getALUUnit();

		// set branchPC
		EX_IF_Latch.setBranchPC(OF_EX_Latch.getBranchTarget());

		// get op1 and op2 from OF_EX_Latch
		int Op1 = OF_EX_Latch.getOp1();
		int Op2;
		
		if(containingProcessor.getControlUnit().isImmediate()){
			Op2 = OF_EX_Latch.getImmx();
			System.out.println("Got immx: " + Integer.toString(Op2));
		}
		else{
			Op2 = OF_EX_Latch.getOp2();
			System.out.println("Got Op2: " + Integer.toString(Op2));
		}

		ALU.setA(Op1);
		ALU.setB(Op2);

		int aluResult = ALU.getALUResult();

		if(CU.isDiv()){
			containingProcessor.getRegisterFile().setValue(31, Op1%Op2);
		}

		if(CU.isAdd()){
			long temp = Op1+Op2;
            if(temp - 2147483647>0){
                String binS = Long.toString(temp);
                String extStr = binS.substring(32);
				int k =  Integer.parseInt(extStr);
				containingProcessor.getRegisterFile().setValue(31, k);
			}
		}

		if(CU.isMul()){
			long temp = Op1*Op2;
            if(temp - 2147483647>0){
                String binS = Long.toString(temp);
                String extStr = binS.substring(32);
				int k =  Integer.parseInt(extStr);
				containingProcessor.getRegisterFile().setValue(31, k);
			}
		}

		if(CU.isSll()){
			long temp = Op1<<Op2;
            if(temp - 2147483647>0){
                String binS = Long.toString(temp);
                String extStr = binS.substring(32);
				int k =  Integer.parseInt(extStr);
				containingProcessor.getRegisterFile().setValue(31, k);
			}
		}
		EX_MA_Latch.setALUResult(aluResult);
		System.out.println("ALU result: " + Integer.toString(aluResult));

		boolean isBranchTaken = false;
		if(CU.isJmp()){isBranchTaken = true;}
		else if(CU.isBeq() && ALU.getFlag(1)){isBranchTaken = true;}
		else if(CU.isBgt() && ALU.getFlag(2)){isBranchTaken = true;}
		else if(CU.isBlt() && ALU.getFlag(3)){isBranchTaken = true;}
		else if(CU.isBne() && ALU.getFlag(4)){isBranchTaken = true;}

		System.out.println("Op1: "+ Integer.toString(Op1));
		System.out.println("Op2: "+ Integer.toString(Op2));


		EX_IF_Latch.setIsBranchTaken(isBranchTaken);
		EX_MA_Latch.setOp2(OF_EX_Latch.getOp2());

		OF_EX_Latch.setEX_enable(false);

		if(isBranchTaken){EX_MA_Latch.setMA_enable(false);}
		else{EX_MA_Latch.setMA_enable(true);}
		
	}

}
