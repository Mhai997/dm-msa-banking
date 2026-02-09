package com.pichincha.dm.msa.banking.service.dto;

import java.time.LocalDate;
import java.util.List;

public record AccountStatementResponseDto(
        Long clientId,
        String clientName,
        String identification,
        LocalDate from,
        LocalDate to,
        List<ReportAccountDto> accounts,
        String pdfBase64
) {}
