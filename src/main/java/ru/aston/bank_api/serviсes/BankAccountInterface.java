package ru.aston.bank_api.serviсes;

import ru.aston.bank_api.domains.Transaction;
import ru.aston.bank_api.dto.AccountProjection;

import java.util.List;

public interface BankAccountInterface {

    public void createAccount(String recipientName, String pin);
    public void depositMoney(int accountNumber, double amount);
    public void withdrawMoney(int accountNumber, String pin, double amount);
    public void transferMoney(int senderAccountNumber, String senderPin, int recipientAccountNumber, double amount);
    public List<AccountProjection> getAllAccounts();
    public List<Transaction> getTransactionHistory(int accountNumber);
}
