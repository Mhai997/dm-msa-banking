package com.pichincha.dm.msa.banking.service;

import com.pichincha.dm.msa.banking.service.dto.AccountStatementResponseDto;

import java.time.LocalDate;

public interface ReportService {
    AccountStatementResponseDto generateAccountStatement(Long clientId, LocalDate from, LocalDate to);
}
