package message;
import java.io.Serializable;

public class DepositMessage implements MessageInterface, Serializable {

  final Status status;
  final int accountNumner;
  final double depositAmount;
  final int pin;

  public DepositMessage(Status status, int account, double depositAmount, int pin) {
    this.status = status;
    this.accountNumner = account;
    this.depositAmount = depositAmount;
    this.pin = pin;
  }

  public DepositMessage(Status status) {
    this(status, -1, -1, -1);
  }

	@Override
	public Status getStatus() {
		return this.status;
	}

  public int getAccountNumner() {
    return this.accountNumner;
  }

  public double getDepositAmount() {
    return this.depositAmount;
  }
  
  public int getPin() {
    return this.pin;
  }

}
