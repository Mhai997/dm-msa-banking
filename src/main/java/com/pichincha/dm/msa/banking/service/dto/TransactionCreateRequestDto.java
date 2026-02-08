package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionCreateRequestDto {

  @NotNull
  private Long accountId;

  @NotNull
  private TransactionType transactionType;

  @NotNull
  @DecimalMin(value = "0.01")
  private BigDecimal amount;
}
