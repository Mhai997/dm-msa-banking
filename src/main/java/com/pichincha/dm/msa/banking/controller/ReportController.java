package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.service.ReportService;
import com.pichincha.dm.msa.banking.service.dto.AccountStatementResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public AccountStatementResponseDto getAccountStatement(
            @RequestParam Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return reportService.generateAccountStatement(clientId, from, to);
    }
}
