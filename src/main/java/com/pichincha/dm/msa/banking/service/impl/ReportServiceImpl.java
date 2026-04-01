package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.domain.Client;
import com.pichincha.dm.msa.banking.domain.Movement;
import com.pichincha.dm.msa.banking.domain.enums.MovementType;
import com.pichincha.dm.msa.banking.exception.NotFoundException;
import com.pichincha.dm.msa.banking.repository.AccountRepository;
import com.pichincha.dm.msa.banking.repository.ClientRepository;
import com.pichincha.dm.msa.banking.repository.MovementRepository;
import com.pichincha.dm.msa.banking.service.ReportService;
import com.pichincha.dm.msa.banking.service.dto.AccountStatementResponseDto;
import com.pichincha.dm.msa.banking.service.dto.ReportAccountDto;
import com.pichincha.dm.msa.banking.service.dto.ReportMovementLineDto;
import com.pichincha.dm.msa.banking.util.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final PdfReportGenerator pdfReportGenerator;

    @Override
    public AccountStatementResponseDto generateAccountStatement(Long clientId, LocalDate from, LocalDate to) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toExclusive = to.plusDays(1).atStartOfDay();

        List<Account> accounts = accountRepository.findByClientClientId(clientId);

        List<ReportAccountDto> accountDtos = accounts.stream().map(acc -> {

            BigDecimal openingBalance = movementRepository
                    .findTopByAccountAccountIdAndMovementDateLessThanOrderByMovementDateDescMovementIdDesc(
                            acc.getAccountId(), fromDt
                    )
                    .map(Movement::getBalance)
                    .orElse(acc.getInitialBalance());

            List<Movement> movements = movementRepository
                    .findByAccountAccountIdAndMovementDateBetween(acc.getAccountId(), fromDt, toExclusive)
                    .stream()
                    .sorted(Comparator.comparing(Movement::getMovementDate)
                            .thenComparing(Movement::getMovementId))
                    .toList();

            BigDecimal totalDebit = movements.stream()
                    .filter(m -> m.getMovementType() == MovementType.RETIRO)
                    .map(m -> m.getAmount().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalCredit = movements.stream()
                    .filter(m -> m.getMovementType() == MovementType.DEPOSITO)
                    .map(m -> m.getAmount().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal closingBalance = movements.isEmpty()
                    ? openingBalance
                    : movements.get(movements.size() - 1).getBalance();

            List<ReportMovementLineDto> lines = new ArrayList<>();

            // Línea inicial del período
            lines.add(new ReportMovementLineDto(
                    from.atStartOfDay(),
                    "SALDO INICIAL DEL PERÍODO",
                    null,
                    BigDecimal.ZERO,
                    openingBalance
            ));

            // Movimientos reales
            lines.addAll(
                    movements.stream()
                            .map(m -> new ReportMovementLineDto(
                                    m.getMovementDate(),
                                    m.getMovementType() == MovementType.RETIRO
                                            ? "TRANSFERENCIA / PAGO"
                                            : "ABONO / DEPÓSITO",
                                    m.getMovementType(),
                                    m.getAmount(),
                                    m.getBalance()
                            ))
                            .toList()
            );

            return new ReportAccountDto(
                    acc.getAccountNumber(),
                    acc.getAccountType(),
                    openingBalance,
                    closingBalance,
                    totalDebit,
                    totalCredit,
                    lines
            );
        }).toList();

        String pdfBase64 = pdfReportGenerator.generateAccountStatementBase64(
                client, from, to, accountDtos
        );

        return new AccountStatementResponseDto(
                client.getClientId(),
                client.getName(),
                client.getIdentification(),
                from,
                to,
                accountDtos,
                pdfBase64
        );
    }
}