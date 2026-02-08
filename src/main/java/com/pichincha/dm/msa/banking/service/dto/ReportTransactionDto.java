package com.pichincha.dm.msa.banking.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReportTransactionDto {
  private LocalDateTime date;
  private String transactionType;
  private BigDecimal amount;
  private BigDecimal balance;
}
