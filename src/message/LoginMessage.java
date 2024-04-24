package message;

import java.io.Serializable;

public class LoginMessage implements MessageInterface, Serializable {
  final Status status;
  final int userId;
  final String password;

  // constructors
  public LoginMessage(Status status, int userId, String password) {
    this.status = status;
    this.userId = userId;
    this.password = password;
  }

  public LoginMessage(Status status) {
    this.status = status;
    this.userId = 0;
    this.password = null;
  }

  @Override
  public Status getStatus() {
    return this.status;
  }

  public int getUserId() {
    return this.userId;
  }

  public String getPassword() {
    return this.password;
  }
}
