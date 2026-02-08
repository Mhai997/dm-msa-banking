package com.pichincha.dm.msa.banking.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDto {

  private Long id;

  private String name;
  private String gender;
  private Integer age;
  private String identification;
  private String address;
  private String phone;

  private String customerId;
  private Boolean status;
}
