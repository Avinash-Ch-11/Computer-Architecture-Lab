package generic;

public class MemoryReadEvent extends Event {

	int addressToReadFrom, newValue;
	
	public MemoryReadEvent(long eventTime, Element requestingElement, Element processingElement, int address) {
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.newValue = -1;
	}

	public MemoryReadEvent(long eventTime, Element requestingElement, Element processingElement, int address, int tValue) {
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.newValue = tValue;	
	}

	public int getNewValue(){return this.newValue;}
	public void setNewValue(int tValue){this.newValue = tValue;}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}


}
