package ru.aston.bank_api.serviсes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.aston.bank_api.domains.BankAccount;
import ru.aston.bank_api.domains.Transaction;
import ru.aston.bank_api.dto.AccountProjection;
import ru.aston.bank_api.dto.AccountProjectionImpl;
import ru.aston.bank_api.repositories.BankAccountRepository;
import ru.aston.bank_api.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private BankAccountService bankAccountService;

    @Before
    public void setUp() {
        bankAccountService = new BankAccountService(bankAccountRepository, transactionRepository);
    }

    @Test
    public void testCreateAccount() {
        String recipientName = "John Doe";
        String pin = "1234";
        BankAccount account = new BankAccount(recipientName, pin, 0.0);
        when(bankAccountRepository.save(ArgumentMatchers.any(BankAccount.class))).thenReturn(account);
        bankAccountService.createAccount(recipientName, pin);
        verify(bankAccountRepository).save(account);
    }

    @Test
    public void testDepositMoney() {
        int accountNumber = 1;
        double amount = 100.0;
        BankAccount account = new BankAccount("John Doe", "1234", 0.0);
        when(bankAccountRepository.findById(accountNumber)).thenReturn(Optional.of(account));

        bankAccountService.depositMoney(accountNumber, amount);

        assertEquals(account.getBalance(), 100.0);
        verify(bankAccountRepository).save(account);
        verify(transactionRepository).save(ArgumentMatchers.any(Transaction.class));
    }

    @Test
    public void testWithdrawMoney() {
        int accountNumber = 1;
        String pin = "1234";
        double amount = 50.0;
        BankAccount account = new BankAccount("John Doe", pin, 100.0);
        when(bankAccountRepository.findByAccountNumberAndPin(accountNumber, pin)).thenReturn(Optional.of(account));

        bankAccountService.withdrawMoney(accountNumber, pin, amount);

        assertEquals(account.getBalance(), 50.0);
        verify(bankAccountRepository).save(account);
        verify(transactionRepository).save(ArgumentMatchers.any(Transaction.class));
    }

    @Test
    public void testTransferMoney() {
        int senderAccountNumber = 1;
        String senderPin = "1234";
        int recipientAccountNumber = 2;
        double amount = 50.0;
        BankAccount senderAccount = new BankAccount("John Doe", senderPin, 100.0);
        BankAccount recipientAccount = new BankAccount("Jane Smith", "5678", 0.0);
        when(bankAccountRepository.findByAccountNumberAndPin(senderAccountNumber, senderPin)).thenReturn(Optional.of(senderAccount));
        when(bankAccountRepository.findById(recipientAccountNumber)).thenReturn(Optional.of(recipientAccount));

        bankAccountService.transferMoney(senderAccountNumber, senderPin, recipientAccountNumber, amount);

        assertEquals(senderAccount.getBalance(), 50.0);
        assertEquals(recipientAccount.getBalance(), 50.0);
        verify(bankAccountRepository, times(2)).save(ArgumentMatchers.any(BankAccount.class));
        verify(transactionRepository, times(2)).save(ArgumentMatchers.any(Transaction.class));
    }

    @Test
    public void testGetAllAccounts() {
        List<AccountProjection> expectedProjections = new ArrayList<>();
        expectedProjections.add(new AccountProjectionImpl("John Doe", 100.0));
        expectedProjections.add(new AccountProjectionImpl("Jane Smith", 200.0));
        when(bankAccountRepository.findAllProjection()).thenReturn(expectedProjections);

        List<AccountProjection> result = bankAccountService.getAllAccounts();

        assertEquals(expectedProjections.size(), result.size());
        assertEquals(expectedProjections, result);
    }

    @Test
    public void testGetTransactionHistory() {
        int accountNumber = 1;
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(accountNumber, Transaction.TransactionType.DEPOSIT, 100.0));
        transactions.add(new Transaction(accountNumber, Transaction.TransactionType.WITHDRAWAL, 50.0));
        when(transactionRepository.findByAccountNumberOrderByTimestampDesc(accountNumber)).thenReturn(transactions);

        List<Transaction> result = bankAccountService.getTransactionHistory(accountNumber);

        assertEquals(transactions, result);
    }
}