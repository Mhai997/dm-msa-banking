package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AccountRequestDto(

        @NotBlank(message = "El número de cuenta es obligatorio")
        @Pattern(regexp = "\\d{6,10}", message = "El número de cuenta debe tener entre 6 y 10 dígitos")
        String accountNumber,

        @NotNull(message = "El tipo de cuenta es obligatorio")
        AccountType accountType,

        @NotNull(message = "El saldo inicial es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
        BigDecimal initialBalance,

        @NotNull(message = "El estado es obligatorio")
        Boolean status,

        @NotNull(message = "El cliente es obligatorio")
        Long clientId

) {}