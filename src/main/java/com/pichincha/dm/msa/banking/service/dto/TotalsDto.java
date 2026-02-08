package com.pichincha.dm.msa.banking.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalsDto {
  private BigDecimal totalCredits;
  private BigDecimal totalDebits;
}
