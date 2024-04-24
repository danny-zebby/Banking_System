package message;

import java.io.Serializable;

public class TransferMessage implements MessageInterface, Serializable {
    final Status status;
    final int fromAccountNumber;
    final int toAccountNumber;
    final double transferAmount;
    final int pin;

    public TransferMessage(Status status, int fromAccountNumber, int toAccountNumber, double transferAmount,
            int pin) {
        this.status = status;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.transferAmount = transferAmount;
        this.pin = pin;
    }

    public TransferMessage(Status status, int fromAccountNumber, int toAccountNumber, double transferAmount) {
        this.status = status;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.transferAmount = transferAmount;
        this.pin = -1;
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
