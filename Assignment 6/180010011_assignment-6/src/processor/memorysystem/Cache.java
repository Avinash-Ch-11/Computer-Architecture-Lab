package processor.memorysystem;

import processor.Processor;
import generic.Event;
import generic.Element;
import java.lang.Math;
import generic.MemoryReadEvent;
import generic.MemoryWriteEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import configuration.Configuration;

public class Cache implements Element {
    static int associativity = 2;
    Processor containingProcessor;
    int cacheSize;
    int latency;
    int nLines;
    int nSetIndBits;
    CacheLine cacheLine[];
    boolean isL1i;
    int LRU[];

    public Cache(){
        cacheSize = -1;
        latency = -1;
        nLines = -1;
    }
    public Cache(Processor containingProcessor, int Latency, int NLines, boolean IsL1i){
        this.cacheSize = NLines*4;
        this.latency = Latency;
        this.nLines = NLines;
        int temp = (int)(Math.log(NLines/2)/Math.log(2));
        this.nSetIndBits = temp;
        this.isL1i = IsL1i;
        this.LRU = new int[128];
        this.containingProcessor = containingProcessor;
        this.cacheLine = new CacheLine[1024];

        for(int i=0; i<1024; i++){cacheLine[i] = new CacheLine();}
        for(int i=0; i<128; i++){this.LRU[i] = 0;}
    }

    public String to32bString(int address){
        String b = String.format("%32s", Integer.toBinaryString(address)).replace(' ','0');
        return b;
    }

    public int fetchTag(String b){
        String tagS = b.substring(0,32-nSetIndBits);
        int tag = Integer.parseInt(tagS, 2);
        return tag;
    }

    public int fetchSetIndex(String b){
        int setIndex = 0;
        if(nSetIndBits!=0){
            String setIndexS = b.substring(32-nSetIndBits, 32);
            setIndex = Integer.parseInt(setIndexS, 2);
        }
        return setIndex;
    }

    public void cacheRead(int add, Element reqElement){
        String b = to32bString(add);
        int tag = fetchTag(b);
        int setIndex = fetchSetIndex(b);
        int lineInd = setIndex*associativity;
        for(int index=lineInd; index<lineInd+associativity; index++){
            if(!cacheLine[index].getIsEmpty()){
                if(cacheLine[index].getTag() == tag){
                    int value = cacheLine[index].getLine()[0];
                    Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime()+latency, this, reqElement, value, add, true));
                    return;
                }
            }
        }
        handleCacheMiss(add);
        return;
    }

    public void printCache(){
        for(int i=0; i<nLines; i++){
            cacheLine[i].printLine();
        }
    }

    public void cacheWrite(int add, int value, Element reqElement){
        String b = to32bString(add);
        int tag = fetchTag(b);
        int setIndex = fetchSetIndex(b);
        int lineIndex = setIndex*associativity;

        for(int index = lineIndex; index<lineIndex+associativity; index++){
            if(!cacheLine[index].getIsEmpty()){
                if(cacheLine[index].getTag() ==tag){
                    cacheLine[index].setData(0, value);//set value to line
                    Simulator.getEventQueue().addEvent(//write event to queue
                        new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), add, value)
                    );

                    Simulator.getEventQueue().addEvent(//response to MA unit
                        new MemoryResponseEvent(Clock.getCurrentTime()+latency, this, reqElement, -1)
                    );

                    return;
                }
            }
        }

        handleCacheMiss(add, value);
        return;
    }

    public void handleCacheMiss(int address){
        Simulator.getEventQueue().addEvent(//Cache miss; Queue Memory read event 
            new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), address));
        return;
    }

    public void handleCacheMiss(int address, int newValue){
        Simulator.getEventQueue().addEvent(//Cache miss; Queue Memory read event
            new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), address, newValue)
            );
        return;
    }
    @Override
    public void handleEvent(Event e){
        MemoryResponseEvent event = (MemoryResponseEvent) e;
        if(isL1i){//For instruction fetch
            event.setEventTime(Clock.getCurrentTime()+latency);
            event.setRequestingElement(this);
            event.setProcessingElement(containingProcessor.getIFUnit());
            Simulator.getEventQueue().addEvent(event);

            int add = event.getAddress();
            String b = to32bString(add);
            int tag = fetchTag(b);
            int setIndex = fetchSetIndex(b);
            int writeIndex = LRU[setIndex];

            LRU[setIndex] = 1-LRU[setIndex];

            cacheLine[(setIndex*associativity)+writeIndex].setData(0, event.getValue());
            cacheLine[(setIndex*associativity)+writeIndex].setTag(tag);
            return;
        }
        else{//L1d data cache
            if(event.getIsRead()){//cache read and response to MA unit
                event.setEventTime(Clock.getCurrentTime()+latency);
                event.setRequestingElement(this);
                event.setProcessingElement(containingProcessor.getMAUnit());
                Simulator.getEventQueue().addEvent(event);

                int add = event.getAddress();
                String b = to32bString(add);
                int tag = fetchTag(b);
                int setIndex = fetchSetIndex(b);
                int writeIndex = LRU[setIndex];

                LRU[setIndex] = 1-LRU[setIndex];//LRU
                cacheLine[setIndex*associativity+writeIndex].setData(0, event.getValue());//write
                cacheLine[setIndex*associativity+writeIndex].setTag(tag);
                return;
            }

            if(event.getValue() == -1){return;}
            //include value of response to cache
            int add = event.getAddress();
            String b = to32bString(add);
            int tag = fetchTag(b);
            int setIndex = fetchSetIndex(b);
            int writeIndex = LRU[setIndex];

            LRU[setIndex] = 1-LRU[setIndex];//LRU

            //write to cache
            cacheLine[setIndex*associativity + writeIndex].setData(0, event.getValue());
            cacheLine[setIndex*associativity + writeIndex].setTag(tag);

            Simulator.getEventQueue().addEvent(//write to main memory
                new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency, this, containingProcessor.getMainMemory(), add, event.getValue())
            );

            //Response to MA uint
            Simulator.getEventQueue().addEvent(
                new MemoryResponseEvent(Clock.getCurrentTime()+latency, this, containingProcessor.getMAUnit(), -1)
            );
        }
    }


}