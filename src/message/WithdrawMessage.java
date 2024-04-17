package message;
import java.io.Serializable;

public class WithdrawMessage implements MessageInterface, Serializable{

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
