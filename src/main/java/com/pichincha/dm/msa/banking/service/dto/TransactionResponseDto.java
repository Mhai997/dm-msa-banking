package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponseDto {

  private Long id;
  private LocalDateTime date;
  private TransactionType transactionType;

  // amount can be signed or positive depending on your decision
  private BigDecimal amount;

  // balance after transaction
  private BigDecimal balance;

  private Long accountId;
}
