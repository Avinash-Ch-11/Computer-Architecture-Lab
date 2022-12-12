package processor.memorysystem;
import generic.Event;
import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;

public class MainMemory implements Element{
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	public void handleEvent(Event eve){
		if(eve.getEventType() == EventType.MemoryRead){
			MemoryReadEvent event = (MemoryReadEvent) eve;
			Simulator.getEventQueue().addEvent(
				new MemoryResponseEvent(Clock.getCurrentTime(), 
				this, 
				event.getRequestingElement(), 
				getWord(event.getAddressToReadFrom()))
			);
		}
		else if(eve.getEventType() == EventType.MemoryWrite){
			MemoryWriteEvent event = (MemoryWriteEvent) eve;
			this.setWord(event.getAddressToWriteTo(), event.getValue());
			Simulator.getEventQueue().addEvent(
				new MemoryResponseEvent(Clock.getCurrentTime(), this, event.getRequestingElement(),-1)
			);
		}
	}
}
