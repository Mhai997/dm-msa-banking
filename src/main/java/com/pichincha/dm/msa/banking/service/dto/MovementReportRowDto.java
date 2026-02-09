package com.pichincha.dm.msa.banking.service.dto;

import java.math.BigDecimal;

public record MovementReportRowDto(
        String date,
        String client,
        String accountNumber,
        String accountType,
        BigDecimal initialBalance,
        Boolean status,
        BigDecimal movement,
        BigDecimal availableBalance
) {}
