package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int immx;
	int branchTarget;
	int op1;
	int op2;
	
	public int getOp1(){
		return this.op1;
	}
	public void setOp1(int op1){
		this.op1 = op1;
	}

	public int getOp2(){
		return this.op2;
	}
	public void setOp2(int op2){
		this.op2 = op2;
	}

	public int getImmx(){
		return this.immx;
	}

	public void setImmx(int immx){
		this.immx = immx;
	}

	public int getBranchTarget(){
		return this.branchTarget;
	}

	public void setBranchTarget(int branchTarget){
		this.branchTarget = branchTarget;
	}


	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean getEX_enable(){
		return this.EX_enable;
	}

	public boolean isEX_enable() {
		return this.EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		this.EX_enable = eX_enable;
	}

}
