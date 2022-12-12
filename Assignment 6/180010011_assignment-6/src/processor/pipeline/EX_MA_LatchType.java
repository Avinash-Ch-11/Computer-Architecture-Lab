package processor.pipeline;
import processor.pipeline.ControlUnit;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int aluResult, op2;
	
	int PC, instruction;
	ControlUnit controlUnit;
	boolean isNop, isControlNop, MA_busy;

	public boolean getIsNop(){return this.isNop;}
	public void setIsNop(boolean isNop){
		this.isNop=isNop;
		if(isNop == true){
			this.PC = -1;
			this.instruction = -1;
			this.op2 = -1;
			this.aluResult = -1;
		}
	}
	public boolean isMA_busy(){return this.MA_busy;}
	public void setMA_busy(boolean ma_busy){this.MA_busy = ma_busy;}
	public boolean getIsControlNop(){return this.isControlNop;}
	public void setIsControlNop(boolean isControlNop){this.isControlNop=isControlNop;}

	public EX_MA_LatchType()
	{
		MA_enable = false;
		isNop = false;
		MA_busy = false;
	}
	
	public int getPC(){return this.PC;}//########tab
	public void setPC(int PC){this.PC = PC;}
	public int getInstruction(){return this.instruction;}
	public void setInstruction(int instruction){this.instruction = instruction;}
	public ControlUnit getControlUnit(){return this.controlUnit;}
	public void setControlUnit(ControlUnit controlUnit){ this.controlUnit = controlUnit;}
	
	public boolean isMA_enable(){return MA_enable;}
	public void setMA_enable(boolean mA_enable){MA_enable = mA_enable;}
	public int getALUResult(){ return this.aluResult;}
	public void setALUResult(int aluResult){ this.aluResult = aluResult;}
	public int getOp2(){ return this.op2;}
	public void setOp2(int op2){ this.op2 = op2;}

}
