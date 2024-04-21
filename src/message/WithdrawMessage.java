package message;

import java.io.Serializable;

public class WithdrawMessage implements MessageInterface, Serializable {
    final int id;
    final Status status;
    final int accountNumber;
    final double withdrawAmount;
    final int pin;

    public WithdrawMessage(int id, Status status, int accountNumber, double withdrawAmount, int pin) {
        this.id = id;
        this.status = status;
        this.accountNumber = accountNumber;
        this.withdrawAmount = withdrawAmount;
        this.pin = pin;
    }

    public WithdrawMessage(int id, Status status) {
        this(id, status, -1, -1, -1);
    }

    @Override
    public int getID() {
        return this.id;
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
