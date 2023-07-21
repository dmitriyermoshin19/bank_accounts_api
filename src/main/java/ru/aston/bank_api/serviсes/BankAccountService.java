package ru.aston.bank_api.serviсes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aston.bank_api.domains.BankAccount;
import ru.aston.bank_api.domains.Transaction;
import ru.aston.bank_api.dto.AccountProjection;
import ru.aston.bank_api.repositories.BankAccountRepository;
import ru.aston.bank_api.repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankAccountService implements BankAccountInterface {

    private BankAccountRepository bankAccountRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createAccount(String recipientName, String pin) {
        BankAccount account = new BankAccount(recipientName, pin, 0.0);
        bankAccountRepository.save(account);
    }

    @Override
    public void depositMoney(int accountNumber, double amount) {
        BankAccount account = bankAccountRepository.findById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account number"));
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
        saveTransaction(accountNumber, Transaction.TransactionType.DEPOSIT, amount);
    }

    @Override
    public void withdrawMoney(int accountNumber, String pin, double amount) {
        BankAccount account = bankAccountRepository.findByAccountNumberAndPin(accountNumber, pin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account number or PIN"));
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in the account");
        }
        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);
        saveTransaction(accountNumber, Transaction.TransactionType.WITHDRAWAL, amount);
    }

    @Override
    public void transferMoney(int senderAccountNumber, String senderPin, int recipientAccountNumber, double amount) {
        BankAccount senderAccount = bankAccountRepository.findByAccountNumberAndPin(senderAccountNumber, senderPin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender account number or PIN"));
        BankAccount recipientAccount = bankAccountRepository.findById(recipientAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipient account number"));
        if (senderAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in the sender account");
        }
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        recipientAccount.setBalance(recipientAccount.getBalance() + amount);
        bankAccountRepository.save(senderAccount);
        bankAccountRepository.save(recipientAccount);
        saveTransaction(senderAccountNumber, Transaction.TransactionType.TRANSFER, -amount);
        saveTransaction(recipientAccountNumber, Transaction.TransactionType.TRANSFER, amount);
    }

    private void saveTransaction(int accountNumber, Transaction.TransactionType type, double amount) {
        if (type.equals(Transaction.TransactionType.DEPOSIT)) {
            Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.DEPOSIT, amount, LocalDateTime.now());
            transactionRepository.save(transaction);
        }
        if (type.equals(Transaction.TransactionType.WITHDRAWAL)) {
            Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.WITHDRAWAL, amount, LocalDateTime.now());
            transactionRepository.save(transaction);
        }
        if (type.equals(Transaction.TransactionType.TRANSFER)) {
            Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.TRANSFER, amount, LocalDateTime.now());
            transactionRepository.save(transaction);
        }
    }

    @Override
    public List<AccountProjection> getAllAccounts() {
        return bankAccountRepository.findAllProjection();
    }

    @Override
    public List<Transaction> getTransactionHistory(int accountNumber) {
        return transactionRepository.findByAccountNumberOrderByTimestampDesc(accountNumber);
    }
}