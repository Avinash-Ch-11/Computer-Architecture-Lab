package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean IF_enable;
	boolean isBranchTaken;
	Integer branchPC;

	public EX_IF_LatchType()
	{
		IF_enable = false;
		isBranchTaken = false;	
	}

	public boolean getIF_enable() { return this.IF_enable;}
	public void setIF_enable(boolean iF_enable){this.IF_enable = iF_enable;}
	public boolean getIsBranchTaken() { return this.isBranchTaken;}
	public void setIsBranchTaken(boolean IsBranchTaken){this.isBranchTaken=IsBranchTaken;}
	public Integer getBranchPC(){return this.branchPC;}
	public void setBranchPC(Integer BranchPC){this.branchPC=BranchPC;}

}
