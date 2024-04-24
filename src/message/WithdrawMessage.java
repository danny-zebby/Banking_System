package message;

import java.io.Serializable;

public class WithdrawMessage implements MessageInterface, Serializable {
    final Status status;
    final int accountNumber;
    final double withdrawAmount;
    final int pin;

    public WithdrawMessage(Status status, int accountNumber, double withdrawAmount, int pin) {
        this.status = status;
        this.accountNumber = accountNumber;
        this.withdrawAmount = withdrawAmount;
        this.pin = pin;
    }

    public WithdrawMessage( Status status) {
        this(status, -1, -1, -1);
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public double getWithdrawAmount() {
        return this.withdrawAmount;
    }

    public int getPin() {
        return this.pin;
    }
}
