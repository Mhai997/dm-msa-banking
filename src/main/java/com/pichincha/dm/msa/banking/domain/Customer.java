package com.pichincha.dm.msa.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer extends Person {

  @Column(unique = true)
  private String customerId;

  private String password;
  private Boolean status;
}
