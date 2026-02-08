package com.pichincha.dm.msa.banking.domain;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String accountNumber;

  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  private BigDecimal initialBalance;
  private Boolean status;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;
}
