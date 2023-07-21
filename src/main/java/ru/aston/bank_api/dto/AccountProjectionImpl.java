package ru.aston.bank_api.dto;

public class AccountProjectionImpl implements AccountProjection {
    private String recipientName;
    private double balance;

    public AccountProjectionImpl(String recipientName, double balance) {
        this.recipientName = recipientName;
        this.balance = balance;
    }

    @Override
    public String getRecipientName() {
        return null;
    }

    @Override
    public double getBalance() {
        return 0;
    }
}
