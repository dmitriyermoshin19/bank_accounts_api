package ru.aston.bank_api.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class DepositRequest {

    @NotNull(message = "Amount must not be null")
    @Min(value = 0, message = "Amount must be greater than or equal to zero")
    private Double amount;

}
