package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementResponseDto(
        Long movementId,
        LocalDateTime movementDate,
        MovementType movementType,
        BigDecimal amount,
        BigDecimal balance,
        Long accountId
) {}

