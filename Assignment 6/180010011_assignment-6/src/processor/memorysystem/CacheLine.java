package processor.memorysystem;
public class CacheLine{
    static int lineSize;
    int data[];
    int tag;
    boolean isEmpty;
    public CacheLine(){
        CacheLine.lineSize = 1;
        data = new int[lineSize];
        tag = -1;
        isEmpty = true;
    }

    public boolean getIsEmpty() {return isEmpty;}
    public void setIsEmpty(boolean IsEmpty) {this.isEmpty = IsEmpty;}

    public int[] getLine(){return this.data;}
    public void setData(int t, int data){
        this.data[t] = data;
        isEmpty = false;
    }

    public int getTag(){return this.tag;}

    public void setTag(int Tag){
        this.tag = Tag;
        isEmpty = false;
    }
    public void printLine(){
        System.out.println(data[0]+": "+tag);
    }
}