package com.pichincha.dm.msa.banking.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportResponseDto {

  private Long customerId;
  private String customerName;

  private String startDate;
  private String endDate;

  private List<ReportAccountDto> accounts;

  private TotalsDto totals;

  // PDF in base64
  private String pdfBase64;
}
