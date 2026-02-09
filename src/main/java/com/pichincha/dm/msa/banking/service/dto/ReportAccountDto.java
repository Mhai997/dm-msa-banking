package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;

import java.math.BigDecimal;
import java.util.List;

public record ReportAccountDto(
        String accountNumber,
        AccountType accountType,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        BigDecimal totalDebit,
        BigDecimal totalCredit,
        List<ReportMovementLineDto> movements
) {}
