package com.pichincha.dm.msa.banking.domain;

import com.pichincha.dm.msa.banking.domain.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id")
  private Long accountId;

  @Column(name = "account_number", nullable = false, length = 30, unique = true)
  private String accountNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_type", nullable = false, length = 20)
  private AccountType accountType;

  @Column(name = "initial_balance", nullable = false, precision = 19, scale = 2)
  private BigDecimal initialBalance;

  @Column(name = "status", nullable = false)
  private Boolean status;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Movement> movements = new ArrayList<>();
}
