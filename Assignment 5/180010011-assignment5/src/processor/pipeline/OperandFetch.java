package processor.pipeline;
import processor.Processor;
import java.util.Scanner;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_busy()){return;}
		if(IF_OF_Latch.getInstruction() == -402653184){
			OF_EX_Latch.setPC(IF_OF_Latch.getPC());
			OF_EX_Latch.setInstruction(IF_OF_Latch.getInstruction());
			OF_EX_Latch.setControlUnit(containingProcessor.getControlUnit());
		}
		if(IF_OF_Latch.isOF_enable())
		{
			// if(cr(IF_OF_Latch.getInstruction())){
			// 	this.containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(false);
			// 	OF_EX_Latch.setEX_enable(true);
			// 	OF_EX_Latch.setIsNop(true);

			// 	containingProcessor.setNumOfStalls(containingProcessor.getNumOfStalls()+1);
			// 	return;
			// }
			// else{
			// 	if(IF_OF_Latch.getInstruction() != -402653184){
			// 		this.containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(true);	
			// 	}
			// }
			containingProcessor.getControlUnit().setOpCode(IF_OF_Latch.getInstruction());
			//TODO
			int ins = IF_OF_Latch.getInstruction();
			String bin = Integer.toBinaryString(ins);;
			if(ins>=0){
				bin = String.format("%32s", Integer.toBinaryString(ins)).replace(' ', '0');
				// bin = String.format("%032s",Integer.toBinaryString(ins));
			}
			System.out.println("Binary ins: "+bin);

			//immx
			String immxS = bin.substring(15);
			if(immxS.charAt(0) == '1'){
				immxS = twoComp(immxS);
				immxS = "-" + immxS;
			}
			int immx = Integer.parseInt(immxS, 2);

			// Calc branchTarget
			String insS = bin.substring(15);
			if(containingProcessor.getControlUnit().isJmp()){
				insS = bin.substring(10);
			}

			if(insS.charAt(0) == '1'){
				insS = twoComp(insS);
				insS = "-" + insS;
			}

			int insI = Integer.parseInt(insS,2);
			System.out.println("offSet: " + insS);
			int branchTarget = insI + IF_OF_Latch.getPC();
			OF_EX_Latch.setBranchTarget(branchTarget);
			System.out.println("branchTarget: " + insI);

			// OP1
			int op1R = Integer.parseInt(bin.substring(5,10), 2);
			// OP2
			int op2R = Integer.parseInt(bin.substring(10,15), 2);
			if(containingProcessor.getControlUnit().isSt()){
				op1R = Integer.parseInt(bin.substring(10,15), 2);
				op2R = Integer.parseInt(bin.substring(5,10), 2);
			}

			int op1 = containingProcessor.getRegisterFile().getValue(op1R);
			int op2 = containingProcessor.getRegisterFile().getValue(op2R);

			System.out.println("op1R: "+op1R+" op1: "+Integer.toString(op1));
			System.out.println("op2R: "+op2R+" op2: "+Integer.toString(op2));
			int opCodeInt = containingProcessor.getControlUnit().getOpCodeInt();
			if(opCodeInt>23 && opCodeInt<29){
				containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(false);
			}

			OF_EX_Latch.setPC(IF_OF_Latch.getPC());
			OF_EX_Latch.setInstruction(IF_OF_Latch.getInstruction());
			OF_EX_Latch.setControlUnit(containingProcessor.getControlUnit());
			OF_EX_Latch.setImmx(immx);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}
	public static char flip(char c) {
        return (c == '0') ? '1' : '0';
    }
	public String twoComp(String bin) {
        int n = bin.length();
        int i;

        String ones = "", twos = "";
        ones = twos = "";

        // for ones complement flip every bit
        for (i = 0; i < n; i++)
        {
            ones += flip(bin.charAt(i));
        }

        twos = ones;
        for (i = n - 1; i >= 0; i--)
        {
            if (ones.charAt(i) == '1')
            {
                twos = twos.substring(0, i) + '0' + twos.substring(i + 1);
            }
            else
            {
                twos = twos.substring(0, i) + '1' + twos.substring(i + 1);
                break;
            }
        }

        // If No break : all are 1 as in 111 or 11111;
        // in such case, add extra 1 at beginning
        if (i == -1)
        {
            twos = '1' + twos;
        }
        return twos;
    }

	public boolean cr (int ins){
		try {
			String bin = Integer.toBinaryString(ins);;
			if(ins>0){
				bin = String.format("%32s", Integer.toBinaryString(ins)).replace(' ','0');
			}

			int op1R = Integer.parseInt(bin.substring(5,10),2);
			int op2R = -1;
			op2R = Integer.parseInt(bin.substring(10,15),2);
			int opCode = containingProcessor.getOpCode(ins);
			if(opCode%2 == 1 && opCode<22){op2R=-1;}
			if(opCode==22){op2R=-1;}

			int OF_EX_ins, EX_MA_ins, MA_RW_ins;

			OF_EX_ins = OF_EX_Latch.getInstruction();
			EX_MA_ins = containingProcessor.getMAUnit().EX_MA_Latch.getInstruction();
			MA_RW_ins = containingProcessor.getRWUnit().MA_RW_Latch.getInstruction();

			int opCode_ox = containingProcessor.getOpCode(OF_EX_ins);
			if(opCode_ox<=22 && !containingProcessor.getOFUnit().OF_EX_Latch.getIsNop()){
				int rdR = containingProcessor.getRd(OF_EX_ins);
				if(op1R == rdR || op2R == rdR){return true;}
			}
			if(containingProcessor.getOpCode(EX_MA_ins)<=22 && !containingProcessor.getEXUnit().EX_MA_Latch.getIsNop()){
				int rdR = containingProcessor.getRd(EX_MA_ins);
				if(op1R == rdR || op2R == rdR){return true;}
			}

			if(containingProcessor.getOpCode(MA_RW_ins)<=22 && !containingProcessor.getMAUnit().MA_RW_Latch.getIsNop()){
				int rdR = containingProcessor.getRd(MA_RW_ins);
				if(op1R == rdR || op2R == rdR){return true;}
			}
		}catch(StringIndexOutOfBoundsException e){return false;}
		return false;
	}
	
}
