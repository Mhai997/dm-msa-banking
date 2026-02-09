package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;

import java.math.BigDecimal;

public record AccountResponseDto(
        Long accountId,
        String accountNumber,
        AccountType accountType,
        BigDecimal initialBalance,
        Boolean status,
        Long clientId
) {}
