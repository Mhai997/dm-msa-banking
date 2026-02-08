package com.pichincha.dm.msa.banking.service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDto {

  @NotBlank
  private String name;

  @NotBlank
  private String gender;

  @NotNull
  @Min(0)
  private Integer age;

  @NotBlank
  private String identification;

  @NotBlank
  private String address;

  @NotBlank
  private String phone;

  @NotBlank
  private String customerId;

  @NotBlank
  private String password;

  @NotNull
  private Boolean status;
}
