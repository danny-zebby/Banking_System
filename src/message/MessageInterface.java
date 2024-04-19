package message;

public interface MessageInterface {
	
	public int getID();
	public String getText();
	public String getTo();
	public String getFrom();
	public Status getStatus();
	public String toString();
	
}
