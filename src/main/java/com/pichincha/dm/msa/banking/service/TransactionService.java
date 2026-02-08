package com.pichincha.dm.msa.banking.service;

import com.pichincha.dm.msa.banking.service.dto.TransactionCreateRequestDto;
import com.pichincha.dm.msa.banking.service.dto.TransactionResponseDto;

public interface TransactionService {
  TransactionResponseDto create(TransactionCreateRequestDto request);
}
