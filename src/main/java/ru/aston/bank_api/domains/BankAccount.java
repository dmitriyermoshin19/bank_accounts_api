package ru.aston.bank_api.domains;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "increment")
    @GenericGenerator(name = "increment", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator")
    @Column(name = "account_number")
    private int accountNumber;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "pin", nullable = false)
    private String pin;

    @Column(name = "balance", nullable = false)
    private double balance;

    public BankAccount() {
    }

    public BankAccount(String recipientName, double balance) {
        this.recipientName = recipientName;
        this.balance = balance;
    }

    public BankAccount(String recipientName, String pin, double balance) {
        this.recipientName = recipientName;
        this.pin = pin;
        this.balance = balance;
    }
}