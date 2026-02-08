package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponseDto {

  private Long id;
  private String accountNumber;
  private AccountType accountType;
  private BigDecimal initialBalance;
  private Boolean status;

  private Long customerId;
}
