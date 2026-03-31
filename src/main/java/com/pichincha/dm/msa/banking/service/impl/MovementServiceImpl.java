package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.domain.Movement;
import com.pichincha.dm.msa.banking.domain.enums.MovementType;
import com.pichincha.dm.msa.banking.exception.BusinessException;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovementServiceImpl implements MovementService {

    private static final String ACCOUNT_NOT_FOUND = "Cuenta no encontrada";
    private static final String MOVEMENT_NOT_FOUND = "Movimiento no encontrado";
    private static final String INSUFFICIENT_BALANCE = "Saldo no disponible";
    private static final String INVALID_AMOUNT = "El valor del movimiento debe ser mayor a cero";
    private static final String INVALID_MOVEMENT_TYPE = "El tipo de movimiento es obligatorio";

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final MovementMapper movementMapper;

    @Override
    public MovementResponseDto create(MovementRequestDto request) {
        validateRequest(request);

        Account account = findAccountById(request.accountId());
        BigDecimal currentBalance = getCurrentBalance(account);
        BigDecimal signedAmount = getSignedAmount(request.amount(), request.movementType());
        BigDecimal newBalance = calculateNewBalance(currentBalance, signedAmount);

        validateAvailableBalance(newBalance);

        Movement movement = buildMovement(request, account, signedAmount, newBalance);
        Movement savedMovement = movementRepository.save(movement);

        return movementMapper.toResponse(savedMovement);
    }

    @Override
    @Transactional(readOnly = true)
    public MovementResponseDto getById(Long movementId) {
        Movement movement = movementRepository.findById(movementId)
                .orElseThrow(() -> new NotFoundException(MOVEMENT_NOT_FOUND));

        return movementMapper.toResponse(movement);
    }

    @Override
    public void delete(Long movementId) {
        if (!movementRepository.existsById(movementId)) {
            throw new NotFoundException(MOVEMENT_NOT_FOUND);
        }
        movementRepository.deleteById(movementId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementResponseDto> findAllMovements() {
        return movementRepository.findAll()
                .stream()
                .map(movementMapper::toResponse)
                .toList();
    }

    private void validateRequest(MovementRequestDto request) {
        if (request == null) {
            throw new BusinessException("La solicitud del movimiento no puede ser nula");
        }

        if (request.movementType() == null) {
            throw new BusinessException(INVALID_MOVEMENT_TYPE);
        }

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(INVALID_AMOUNT);
        }
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));
    }

    private BigDecimal getCurrentBalance(Account account) {
        return movementRepository
                .findTopByAccountAccountIdOrderByMovementDateDescMovementIdDesc(account.getAccountId())
                .map(Movement::getBalance)
                .orElse(account.getInitialBalance());
    }

    private BigDecimal getSignedAmount(BigDecimal amount, MovementType movementType) {
        BigDecimal absoluteAmount = amount.abs();

        if (movementType == MovementType.DEBIT) {
            return absoluteAmount.negate();
        }

        return absoluteAmount;
    }

    private BigDecimal calculateNewBalance(BigDecimal currentBalance, BigDecimal signedAmount) {
        return currentBalance.add(signedAmount);
    }

    private void validateAvailableBalance(BigDecimal newBalance) {
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(INSUFFICIENT_BALANCE);
        }
    }

    private Movement buildMovement(MovementRequestDto request,
                                   Account account,
                                   BigDecimal signedAmount,
                                   BigDecimal newBalance) {
        Movement movement = movementMapper.toEntity(request);
        movement.setAccount(account);
        movement.setAmount(signedAmount);
        movement.setBalance(newBalance);
        movement.setMovementDate(
                request.movementDate() != null ? request.movementDate() : LocalDateTime.now()
        );
        return movement;
    }
}