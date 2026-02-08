package com.pichincha.dm.msa.banking.domain;

import com.pichincha.dm.msa.banking.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime date;

  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;

  private BigDecimal amount;

  // balance after transaction
  private BigDecimal balance;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;
}
