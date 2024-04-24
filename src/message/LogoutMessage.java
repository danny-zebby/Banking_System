package message;
import java.io.Serializable;

public class LogoutMessage implements MessageInterface, Serializable {

    final Status status;

    public LogoutMessage(Status status) {
      this.status = status;
    }

    @Override
    public Status getStatus() {
      return this.status;
    }

}
