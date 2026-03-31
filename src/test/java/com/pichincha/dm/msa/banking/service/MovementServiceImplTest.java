package com.pichincha.dm.msa.banking.service;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.domain.Movement;
import com.pichincha.dm.msa.banking.domain.enums.MovementType;
import com.pichincha.dm.msa.banking.exception.InsufficientBalanceException;
import com.pichincha.dm.msa.banking.exception.NotFoundException;
import com.pichincha.dm.msa.banking.repository.AccountRepository;
import com.pichincha.dm.msa.banking.repository.MovementRepository;
import com.pichincha.dm.msa.banking.service.dto.MovementRequestDto;
import com.pichincha.dm.msa.banking.service.dto.MovementResponseDto;
import com.pichincha.dm.msa.banking.service.impl.MovementServiceImpl;
import com.pichincha.dm.msa.banking.service.mapper.MovementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MovementMapper movementMapper;

    @InjectMocks
    private MovementServiceImpl movementService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId(1L);
        account.setInitialBalance(new BigDecimal("100.00"));
    }

    @Test
    void shouldCreateCreditMovementSuccessfully() {
        MovementRequestDto request = new MovementRequestDto(
                LocalDateTime.now(),
                MovementType.CREDIT,
                new BigDecimal("50.00"),
                1L
        );

        Movement movementEntity = new Movement();
        Movement savedMovement = new Movement();
        savedMovement.setMovementId(1L);
        savedMovement.setAccount(account);
        savedMovement.setAmount(new BigDecimal("50.00"));
        savedMovement.setBalance(new BigDecimal("150.00"));
        savedMovement.setMovementType(MovementType.CREDIT);

        MovementResponseDto responseDto = mock(MovementResponseDto.class);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.findTopByAccountAccountIdOrderByMovementDateDescMovementIdDesc(1L))
                .thenReturn(Optional.empty());
        when(movementMapper.toEntity(request)).thenReturn(movementEntity);
        when(movementRepository.save(any(Movement.class))).thenReturn(savedMovement);
        when(movementMapper.toResponse(savedMovement)).thenReturn(responseDto);

        MovementResponseDto result = movementService.create(request);

        assertNotNull(result);

        ArgumentCaptor<Movement> captor = ArgumentCaptor.forClass(Movement.class);
        verify(movementRepository).save(captor.capture());

        Movement capturedMovement = captor.getValue();
        assertEquals(new BigDecimal("50.00"), capturedMovement.getAmount());
        assertEquals(new BigDecimal("150.00"), capturedMovement.getBalance());
        assertEquals(account, capturedMovement.getAccount());
    }

    @Test
    void shouldCreateDebitMovementSuccessfully() {
        MovementRequestDto request = new MovementRequestDto(
                LocalDateTime.now(),
                MovementType.DEBIT,
                new BigDecimal("30.00"),
                1L
        );

        Movement lastMovement = new Movement();
        lastMovement.setBalance(new BigDecimal("100.00"));

        Movement movementEntity = new Movement();
        Movement savedMovement = new Movement();
        savedMovement.setMovementId(2L);
        savedMovement.setAccount(account);
        savedMovement.setAmount(new BigDecimal("-30.00"));
        savedMovement.setBalance(new BigDecimal("70.00"));
        savedMovement.setMovementType(MovementType.DEBIT);

        MovementResponseDto responseDto = mock(MovementResponseDto.class);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.findTopByAccountAccountIdOrderByMovementDateDescMovementIdDesc(1L))
                .thenReturn(Optional.of(lastMovement));
        when(movementMapper.toEntity(request)).thenReturn(movementEntity);
        when(movementRepository.save(any(Movement.class))).thenReturn(savedMovement);
        when(movementMapper.toResponse(savedMovement)).thenReturn(responseDto);

        MovementResponseDto result = movementService.create(request);

        assertNotNull(result);

        ArgumentCaptor<Movement> captor = ArgumentCaptor.forClass(Movement.class);
        verify(movementRepository).save(captor.capture());

        Movement capturedMovement = captor.getValue();
        assertEquals(new BigDecimal("-30.00"), capturedMovement.getAmount());
        assertEquals(new BigDecimal("70.00"), capturedMovement.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        MovementRequestDto request = new MovementRequestDto(
                LocalDateTime.now(),
                MovementType.DEBIT,
                new BigDecimal("150.00"),
                1L
        );

        Movement lastMovement = new Movement();
        lastMovement.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.findTopByAccountAccountIdOrderByMovementDateDescMovementIdDesc(1L))
                .thenReturn(Optional.of(lastMovement));

        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> movementService.create(request)
        );

        assertEquals("Saldo no disponible", exception.getMessage());
        verify(movementRepository, never()).save(any(Movement.class));
    }

    @Test
    void shouldUseInitialBalanceWhenNoPreviousMovementsExist() {
        MovementRequestDto request = new MovementRequestDto(
                LocalDateTime.now(),
                MovementType.CREDIT,
                new BigDecimal("25.00"),
                1L
        );

        account.setInitialBalance(new BigDecimal("200.00"));

        Movement movementEntity = new Movement();
        Movement savedMovement = new Movement();
        savedMovement.setMovementId(3L);
        savedMovement.setAccount(account);
        savedMovement.setAmount(new BigDecimal("25.00"));
        savedMovement.setBalance(new BigDecimal("225.00"));

        MovementResponseDto responseDto = mock(MovementResponseDto.class);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(movementRepository.findTopByAccountAccountIdOrderByMovementDateDescMovementIdDesc(1L))
                .thenReturn(Optional.empty());
        when(movementMapper.toEntity(request)).thenReturn(movementEntity);
        when(movementRepository.save(any(Movement.class))).thenReturn(savedMovement);
        when(movementMapper.toResponse(savedMovement)).thenReturn(responseDto);

        MovementResponseDto result = movementService.create(request);

        assertNotNull(result);

        ArgumentCaptor<Movement> captor = ArgumentCaptor.forClass(Movement.class);
        verify(movementRepository).save(captor.capture());

        Movement capturedMovement = captor.getValue();
        assertEquals(new BigDecimal("225.00"), capturedMovement.getBalance());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAccountDoesNotExist() {
        MovementRequestDto request = new MovementRequestDto(
                LocalDateTime.now(),
                MovementType.CREDIT,
                new BigDecimal("50.00"),
                99L
        );

        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> movementService.create(request)
        );

        assertEquals("Cuenta no encontrada", exception.getMessage());
        verify(movementRepository, never()).save(any(Movement.class));
    }
}