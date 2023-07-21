package ru.aston.bank_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.aston.bank_api.domains.BankAccount;
import ru.aston.bank_api.dto.AccountProjection;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository  extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByAccountNumberAndPin(int senderAccountNumber, String senderPin);
    @Query("SELECT b.recipientName AS recipientName, b.balance AS balance FROM BankAccount b")
    List<AccountProjection> findAllProjection();
}
