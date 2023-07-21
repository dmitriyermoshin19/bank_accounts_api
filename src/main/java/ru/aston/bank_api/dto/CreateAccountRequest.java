package ru.aston.bank_api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateAccountRequest {

        @NotBlank(message = "Recipient name must not be blank")
        private String recipientName;

        @NotBlank(message = "PIN must not be blank")
        @Size(min = 4, max = 4, message = "PIN must be exactly 4 characters long")
        private String pin;

}
