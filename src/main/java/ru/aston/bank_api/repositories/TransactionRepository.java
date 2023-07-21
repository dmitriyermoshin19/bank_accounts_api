package ru.aston.bank_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aston.bank_api.domains.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccountNumberOrderByTimestampDesc(int accountNumber);
}
