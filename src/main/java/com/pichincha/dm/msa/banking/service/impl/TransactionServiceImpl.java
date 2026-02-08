package com.pichincha.dm.msa.banking.service.impl;

import com.pichincha.dm.msa.banking.domain.Account;
import com.pichincha.dm.msa.banking.domain.Transaction;
import com.pichincha.dm.msa.banking.domain.enums.TransactionType;
import com.pichincha.dm.msa.banking.exception.BusinessException;
import com.pichincha.dm.msa.banking.exception.NotFoundException;
import com.pichincha.dm.msa.banking.repository.AccountRepository;
import com.pichincha.dm.msa.banking.repository.TransactionRepository;
import com.pichincha.dm.msa.banking.service.TransactionService;
import com.pichincha.dm.msa.banking.service.dto.TransactionCreateRequestDto;
import com.pichincha.dm.msa.banking.service.dto.TransactionResponseDto;
import com.pichincha.dm.msa.banking.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;

  @Override
  public TransactionResponseDto create(TransactionCreateRequestDto request) {

    Account account = accountRepository.findById(request.getAccountId())
        .orElseThrow(() -> new NotFoundException("Account not found"));

    BigDecimal currentBalance = transactionRepository
        .findTopByAccountIdOrderByDateDesc(account.getId())
        .map(Transaction::getBalance)
        .orElse(account.getInitialBalance());

    BigDecimal amount = request.getAmount();

    if (request.getTransactionType() == TransactionType.DEBIT) {
      // regla negocio: no permitir sobregiro
      if (currentBalance.compareTo(amount) < 0) {
        throw new BusinessException("Insufficient balance");
      }
      amount = amount.negate();
    }

    BigDecimal newBalance = currentBalance.add(amount);

    Transaction tx = transactionMapper.toEntity(request);
    tx.setDate(LocalDateTime.now());
    tx.setAccount(account);
    tx.setAmount(amount);
    tx.setBalance(newBalance);

    Transaction saved = transactionRepository.save(tx);

    return transactionMapper.toDto(saved);
  }
}
