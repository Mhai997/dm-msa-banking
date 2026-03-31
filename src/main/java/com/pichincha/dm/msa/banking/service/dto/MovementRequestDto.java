package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.MovementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementRequestDto(

        LocalDateTime movementDate,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        MovementType movementType,

        @NotNull(message = "El valor del movimiento es obligatorio")
        @Positive(message = "El valor del movimiento debe ser mayor a cero")
        BigDecimal amount,

        @NotBlank(message = "El número de cuenta es obligatorio")
        String accountNumber

) {}