package message;
import java.io.Serializable;

public class LogoutMessage implements MessageInterface, Serializable {

    final Status status;
    final LogoutMessageType type;
    
 // used for bankuser logout
    public LogoutMessage(Status status) {
    	this.status = status;
    	this.type = LogoutMessageType.BANK_USER;
    }
    
    // used for bankuser logout
    public LogoutMessage(Status status, LogoutMessageType type) {
    	this.status = status;
    	this.type = type;
    }

    @Override
    public Status getStatus() {
      return this.status;
    }
    
    public LogoutMessageType getType() {
    	return this.type;
    }

}
