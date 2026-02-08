package com.pichincha.dm.msa.banking.controller;

import com.pichincha.dm.msa.banking.service.TransactionService;
import com.pichincha.dm.msa.banking.service.dto.TransactionCreateRequestDto;
import com.pichincha.dm.msa.banking.service.dto.TransactionResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TransactionResponseDto create(@Valid @RequestBody TransactionCreateRequestDto request) {
    return transactionService.create(request);
  }
}
