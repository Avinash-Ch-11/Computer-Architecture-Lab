package processor.pipeline;
import processor.pipeline.ControlUnit;
import processor.Processor;

public class ArithmeticLogicUnit{
    int A;
    int B;
    ControlUnit controlUnit;
    

    public ArithmeticLogicUnit(){
        
    }

    public ControlUnit getControlUnit(){
        return this.controlUnit;
    }

    public void setControlUnit(ControlUnit controlUnit){
        this.controlUnit = controlUnit;
    }

    public int getA() {return this.A;}

    public void setA(int A){this.A = A;}

    public int getB() {return this.B;}

    public void setB(int B){this.B = B;}

    public int getALUResult(){
        
        if(controlUnit.isLd() || controlUnit.isSt()){
            return A+B;
        }
        
        if(controlUnit.isAdd()){return A+B;}
        if(controlUnit.isSub()){return A-B;}
        if(controlUnit.isMul()){return A*B;}
        if(controlUnit.isDiv()){return A / B;}

        if(controlUnit.isAnd()){return A&B;}
        if(controlUnit.isOr()){return A|B;}
        if(controlUnit.isXor()){return A^B;}
        if(controlUnit.isSlt()){return A<B?1:0;}

        if(controlUnit.isSll()){return A<<B;}
        if(controlUnit.isSrl()){return A>> B;}
        if(controlUnit.isSra()){return A >>>B;}

        return 0;
    }

    public boolean getFlag(int type){
        switch(type){
            case 1: return A == B;
            case 2: return A>B;
            case 3: return A<B;
            case 4: return A != B;
        }
        return false;
    }
}