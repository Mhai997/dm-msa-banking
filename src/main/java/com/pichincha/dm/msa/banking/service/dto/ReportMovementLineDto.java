package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportMovementLineDto(
        LocalDateTime movementDate,
        String description,
        MovementType movementType,
        BigDecimal amount,
        BigDecimal balance
) {}