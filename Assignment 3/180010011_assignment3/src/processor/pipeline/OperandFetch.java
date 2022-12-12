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
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			int ins = IF_OF_Latch.getInstruction();
			String bin;
			if(ins>=0){
				bin = Integer.toBinaryString(ins);
				bin = String.format("%32s", Integer.toBinaryString(ins)).replace(' ', '0');
				// bin = String.format("%032s",Integer.toBinaryString(ins));
			}
			else{
				bin = Integer.toBinaryString(ins);
			}
			System.out.println("Binary ins: "+bin);

			//immx
			String immxS = bin.substring(15);
			if(immxS.charAt(0) == '1'){
				immxS = twoComp(immxS);
				immxS = "-" + immxS;
			}
			int immx = Integer.parseInt(immxS, 2);
			OF_EX_Latch.setImmx(immx);

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
			int branchTarget = insI + containingProcessor.getRegisterFile().getProgramCounter()-1;
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

}
