package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.domain.Movement;
import com.pichincha.dm.msa.banking.domain.enums.MovementType;
import com.pichincha.dm.msa.banking.exception.InsufficientBalanceException;
import com.pichincha.dm.msa.banking.exception.NotFoundException;
import com.pichincha.dm.msa.banking.repository.AccountRepository;
import com.pichincha.dm.msa.banking.repository.MovementRepository;
import com.pichincha.dm.msa.banking.service.MovementService;
import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;
import com.pichincha.dm.msa.banking.service.mapper.MovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final MovementMapper movementMapper;

    @Override
    public MovementResponseDto create(MovementRequestDto dto) {
        Account account = accountRepository.findById(dto.accountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        BigDecimal currentBalance = movementRepository
                .findTopByAccountAccountIdOrderByMovementDateDescMovementIdDesc(account.getAccountId())
                .map(Movement::getBalance)
                .orElse(account.getInitialBalance());

        // Normalizamos el monto según el tipo (regla: crédito +, débito -)
        BigDecimal rawAmount = dto.amount() == null ? BigDecimal.ZERO : dto.amount().abs();
        BigDecimal signedAmount = (dto.movementType() == MovementType.DEBIT)
                ? rawAmount.negate()
                : rawAmount;

        BigDecimal newBalance = currentBalance.add(signedAmount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        Movement movement = movementMapper.toEntity(dto);
        movement.setAccount(account);
        movement.setAmount(signedAmount);
        movement.setBalance(newBalance);
        movement.setMovementDate(dto.movementDate() != null ? dto.movementDate() : LocalDateTime.now());

        Movement saved = movementRepository.save(movement);
        return movementMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MovementResponseDto getById(Long movementId) {
        Movement movement = movementRepository.findById(movementId)
                .orElseThrow(() -> new NotFoundException("Movement not found"));
        return movementMapper.toResponse(movement);
    }

    @Override
    public void delete(Long movementId) {
        if (!movementRepository.existsById(movementId)) {
            throw new NotFoundException("Movement not found");
        }
        movementRepository.deleteById(movementId);
    }
}
