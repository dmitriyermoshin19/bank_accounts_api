package ru.aston.bank_api.domains;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment1")
    @GenericGenerator(name = "increment1", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "account_number", nullable = false)
    private int accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public Transaction() {
    }

    public Transaction(int accountNumber, TransactionType type, double amount) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
    }

    public Transaction(int accountNumber, TransactionType type, double amount, LocalDateTime timestamp) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}