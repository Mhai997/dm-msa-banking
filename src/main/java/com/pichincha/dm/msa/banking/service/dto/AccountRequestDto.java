package com.pichincha.dm.msa.banking.service.dto;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequestDto {

  @NotBlank
  private String accountNumber;

  @NotNull
  private AccountType accountType;

  @NotNull
  @DecimalMin(value = "0.00")
  private BigDecimal initialBalance;

  @NotNull
  private Boolean status;

  @NotNull
  private Long customerId; // FK (Customer.id)
}
