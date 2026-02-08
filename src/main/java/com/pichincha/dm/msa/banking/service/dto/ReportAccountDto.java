package com.pichincha.dm.msa.banking.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportAccountDto {
  private Long accountId;
  private String accountNumber;
  private String accountType;

  private List<ReportTransactionDto> transactions;
}
