package processor.pipeline;

public class ControlUnit{
    String opCode;
    int opCodeInt;
    boolean isOverflow;
    int overflow;

    public ControlUnit(){
        this.isOverflow = false;
    }

    public int getOverflow() {//not req
        return this.overflow;
    }

    public void setOverflow(int overflow){
        this.overflow = overflow;
    }

    public boolean getIsOverflow() {
        return this.isOverflow;
    }

    public void setIsOverflow(boolean isOverflow){
        this.isOverflow = isOverflow;
    }

    public String getOpCode(){
        return this.opCode;
    }

    public void setOpCode(int instruction){
        int ins = instruction;
		String bin;
			if(ins>=0){
				bin = Integer.toBinaryString(ins);
				bin = String.format("%32s", Integer.toBinaryString(ins)).replace(' ', '0');
			}
			else{
				bin = Integer.toBinaryString(ins);
			}
        this.opCode = bin.substring(0,5);
        this.opCodeInt = Integer.parseInt(opCode,2);
        System.out.println("OPcode: "+ opCode);
    }

    public int getOpCodeInt() {return this.opCodeInt;}

    // Memory ins flags
    public boolean isSt() {
        if(opCodeInt == 23){
            System.out.println("Store");
            return true;}
        return false;
    }

    public boolean isLd() {
        if(opCodeInt == 22){return true;}
        return false;
    }
    // Control flow instruction flags
    public boolean isJmp() {
        if(opCodeInt == 24){return true;}
        return false;
    }

    public boolean isBeq() {
        if(opCodeInt == 25){return true;}
        return false;
    }

    public boolean isBne() {
        if(opCodeInt == 26){return true;}
        return false;
    }

    public boolean isBlt(){
        if(opCodeInt == 27){return true;}
        return false;
    }
    public boolean isBgt(){
        if(opCodeInt == 28){return true;}
        return false;
    }

    //Arithmetic ins flags
    public boolean isR3(){
        if(opCodeInt < 21 && (opCodeInt %2 == 0)){return true;}
        return false;
    }
    public boolean isImmediate(){
        if(opCodeInt <=23 && (opCodeInt%2 == 1)){return true;}
        else if(opCodeInt == 22){return true;}
        return false;
    }

    public boolean isWb(){
        if(opCodeInt >= 23){return false;}
        return true;
    }

    public boolean isAdd(){
        if(opCodeInt == 0 || opCodeInt == 1 ){return true;}
        return false;
    }
    public boolean isSub(){
        if(opCodeInt == 2 || opCodeInt == 3){return true;}
        return false;
    }
    public boolean isMul(){
        if(opCodeInt == 4 || opCodeInt ==5){return true;}
        return false;
    }
    public boolean isDiv(){
        if(opCodeInt == 6 || opCodeInt ==7){return true;}
        return false;
    }
    public boolean isAnd(){
        if(opCodeInt == 8 || opCodeInt ==9){return true;}
        return false;
    }
    public boolean isOr(){
        if(opCodeInt ==10 || opCodeInt ==11){return true;}
        return false;
    }
    public boolean isXor(){
        if(opCodeInt ==12 || opCodeInt ==13){return true;}
        return false;
    }
    public boolean isSlt(){
        if(opCodeInt ==14 || opCodeInt ==15){return true;}
        return false;
    }
    public boolean isSll(){
        if(opCodeInt ==16 || opCodeInt ==17){return true;}
        return false;
    }
    public boolean isSrl() {
        if(opCodeInt ==18 || opCodeInt ==19){return true;}
        return false;
    }
    public boolean isSra(){
        if(opCodeInt ==20 || opCodeInt ==21){return true;}
        return false;
    }
    public boolean isEnd() {
        if(opCodeInt == 29){return true;}
        return false;
    }
}