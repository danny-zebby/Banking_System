package message;
import java.io.Serializable;

public class LogoutMessage implements MessageInterface, Serializable {

    final int id;
    final Status status;

    // constructors
    public LogoutMessage(int id, Status status) {
      this.id = id;
      this.status = status;
    }

    // getters
    @Override
    public int getID() {
      return this.id;
    }

    @Override
    public Status getStatus() {
      return this.status;
    }

}
