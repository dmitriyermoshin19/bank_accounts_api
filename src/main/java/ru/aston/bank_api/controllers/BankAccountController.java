package ru.aston.bank_api.controllers;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.aston.bank_api.domains.Transaction;
import ru.aston.bank_api.dto.*;
import ru.aston.bank_api.serviсes.BankAccountService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Validated
@Api(tags = "BankAccountController")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        bankAccountService.createAccount(request.getRecipientName(), request.getPin());
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> depositMoney(
            @PathVariable @Min(1) int accountNumber,
            @Valid @RequestBody DepositRequest request) {
        bankAccountService.depositMoney(accountNumber, request.getAmount());
        return ResponseEntity.ok("Money deposited successfully");
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> withdrawMoney(
            @PathVariable @Min(1) int accountNumber,
            @Valid @RequestBody WithdrawRequest request) {
        bankAccountService.withdrawMoney(accountNumber, request.getPin(), request.getAmount());
        return ResponseEntity.ok("Money withdrawn successfully");
    }

    @PostMapping("/{senderAccountNumber}/transfer/{recipientAccountNumber}")
    public ResponseEntity<String> transferMoney(
            @PathVariable @Min(1) int senderAccountNumber,
            @PathVariable @Min(1) int recipientAccountNumber,
            @Valid @RequestBody TransferRequest request) {
        bankAccountService.transferMoney(senderAccountNumber, request.getSenderPin(), recipientAccountNumber, request.getAmount());
        return ResponseEntity.ok("Money transferred successfully");
    }

    @GetMapping
    public ResponseEntity<List<AccountProjection>> getAllAccounts() {
        List<AccountProjection> accounts = bankAccountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(
            @PathVariable @Min(1) int accountNumber) {
        List<Transaction> transactions = bankAccountService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }
}

