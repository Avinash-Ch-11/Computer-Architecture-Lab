package generic;

public class MemoryResponseEvent extends Event {

	int value, address;
	boolean isRead;
	
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.isRead = false;
	}

	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value, int address, boolean isRead) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.address = address;
		this.isRead = isRead;
	}
	
	public int getAddress(){return this.address;}
	public void setAddress(int address){this.address = address;}

	public boolean getIsRead(){return this.isRead;}
	public void setIsRead(boolean isRead){this.isRead = isRead;}

	public int getValue(){return this.value;}
	public void setValue(int value){this.value = value;}

}
