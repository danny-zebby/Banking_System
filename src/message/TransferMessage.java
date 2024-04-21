package message;
import java.io.Serializable;

public class TransferMessage implements MessageInterface, Serializable{
  final int id;
  final Status status;
  final int fromAccountNumber;
  final int toAccountNumber;
  final double transferAmount;
  final int pin;

  public TransferMessage(int id, Status status, int fromAccountNumber, int toAccountNumber, double transferAmount, int pin) {
    this.id = id;
    this.status = status;
    this.fromAccountNumber = fromAccountNumber;
    this.toAccountNumber = toAccountNumber;
    this.transferAmount = transferAmount;
    this.pin = pin;
  }

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public Status getStatus() {
		return this.status;
	}

  public int getFromAccountNumber() {
    return this.fromAccountNumber;
  }

  public int getToAccountNumber() {
    return this.toAccountNumber;
  }

  public double getTransferAmount() {
    return this.transferAmount;
  }

  public int getPin() {
    return this.pin;
  }

}
