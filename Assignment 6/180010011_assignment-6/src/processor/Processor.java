package processor;

import processor.memorysystem.MainMemory;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
import processor.pipeline.OperandFetch;
import processor.pipeline.RegisterFile;
import processor.pipeline.RegisterWrite;
import processor.pipeline.ControlUnit;
import generic.Simulator;
import processor.pipeline.ArithmeticLogicUnit;
import processor.memorysystem.Cache;
import configuration.Configuration;

public class Processor {
	
	Cache L1i_cache, L1d_cache;

	RegisterFile registerFile;
	MainMemory mainMemory;
	
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	InstructionFetch IFUnit;
	OperandFetch OFUnit;
	Execute EXUnit;
	MemoryAccess MAUnit;
	RegisterWrite RWUnit;

	Simulator simulator;

	ControlUnit controlUnit;
	ArithmeticLogicUnit arithmeticLogicUnit;
	boolean isEnd;
	
	//Stats
	int numIns;
	int numCycles;
	int numOfStalls;
	int numOfBranchTaken;

	boolean OF_EX_Nop;
	Boolean EX_MA_Nop;
	boolean MA_RW_Nop;

	public Processor()
	{
		
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();
		L1i_cache = new Cache(this, Configuration.L1i_latency, Configuration.L1i_numberOfLines, true);
		L1d_cache = new Cache(this, Configuration.L1d_latency, Configuration.L1d_numberOfLines, false);

		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType();
		MA_RW_Latch = new MA_RW_LatchType();
		
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch);
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch);

		controlUnit = new ControlUnit();
		arithmeticLogicUnit = new ArithmeticLogicUnit();
		isEnd = false;

		numIns = 0;
		numCycles = 0;
		numOfStalls = 0;
		numOfBranchTaken =0;

		// OF_EX_Nop = false;
		// EX_MA_Nop = false;
		// MA_RW_Nop = false;
	}
	
		// public void setOF_EX_Nop(boolean OF_EX_Nop){
		// 	this.OF_EX_Nop = OF_EX_Nop;
		// }
	public Cache getL1d_cache(){return this.L1d_cache;}
	public void setL1d_cache(Cache l1d_cache){this.L1d_cache=l1d_cache;}

	public Cache getL1i_cache(){return this.L1i_cache;}
	public void setL1i_cache(Cache l1i_cache){this.L1i_cache=l1i_cache;}
	
	public Simulator getSimulator(Simulator sim){return this.simulator;}
	public void setSimulator(Simulator sim){this.simulator = sim;}
	
	public void setEX_MA_Nop(boolean EX_MA_Nop){
		this.EX_MA_Nop = EX_MA_Nop;
	}
	public void setMA_RW_Nop(boolean MA_RW_Nop){
		this.MA_RW_Nop = MA_RW_Nop;
	}

	// public boolean getOF_EX_Nop(){
	// 	return this.OF_EX_Nop;
	// }
	public boolean getEX_MA_Nop(){
		return this.EX_MA_Nop;
	}
	public boolean getMA_RW_Nop(){
		return this.MA_RW_Nop;
	}

	
	public void printState(int memoryStartingAddress, int memoryEndingAddress)
	{
		System.out.println(registerFile.getContentsAsString());
		
		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));		
	}

	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public ControlUnit getControlUnit() {
		return controlUnit;
	}
	public void setControlUnit(ControlUnit controlUnit1){
		this.controlUnit=controlUnit1;
	}

	public ArithmeticLogicUnit getALUUnit(){
		return this.arithmeticLogicUnit;
	}
	public void setALUnit(ArithmeticLogicUnit arithmeticLogicUnit){
		this.arithmeticLogicUnit=arithmeticLogicUnit;
	}
	public InstructionFetch getIFUnit() {
		return IFUnit;
	}

	public OperandFetch getOFUnit() {
		return OFUnit;
	}

	public Execute getEXUnit() {
		return EXUnit;
	}

	public MemoryAccess getMAUnit() {
		return MAUnit;
	}

	public RegisterWrite getRWUnit() {
		return RWUnit;
	}

	public boolean getIsEnd(){
		return this.isEnd;
	}
	public void setIsEnd(boolean isEnd){
		this.isEnd = isEnd;
	}

	public int getNumIns(){
		return this.numIns;
	}
	public void setNumIns(int numIns){
		this.numIns = numIns;
	}
	public int getNumCycles(){
		return this.numCycles;
	}
	public void setNumCycles(int numCycles){
		this.numCycles = numCycles;
	}

	public int getNumOfStalls(){
		return this.numOfStalls;
	}
	public void setNumOfStalls(int numOfStalls){
		this.numOfStalls = numOfStalls;
	}
	public void setNumOfBranchTaken(int numOfBranchTaken){
		this.numOfBranchTaken = numOfBranchTaken;
	}
	public int getNumOfBranchTaken(){
		return this.numOfBranchTaken;
	}

	public int getRd(int instruction){
		ControlUnit CU = new ControlUnit();
		CU.setOpCode(instruction);
		String insS = Integer.toBinaryString(instruction);
		if(instruction>0){
			insS = String.format("%32s", Integer.toBinaryString(instruction)).replace(' ', '0');
		}
		String rdS = insS.substring(10,15);
		if(CU.isJmp()){
			rdS = insS.substring(5,10);
		}
		if(CU.isR3()){rdS = insS.substring(15,20);}
		int rd = Integer.parseInt(rdS, 2);
		return rd;
	}

	public int getOpCode(int instruction){
		String binS = Integer.toBinaryString(instruction);
		if(instruction>0){
			binS = String.format("%32s", Integer.toBinaryString(instruction)).replace(' ','0');
		}
		String opCode = binS.substring(0,5);
		return Integer.parseInt(opCode, 2);
	}
	
}