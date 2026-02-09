package com.pichincha.dm.msa.banking.domain;

import com.pichincha.dm.msa.banking.domain.enums.MovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "movement")
public class Movement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "movement_id")
  private Long movementId;

  @Column(name = "movement_date", nullable = false)
  private LocalDateTime movementDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "movement_type", nullable = false, length = 20)
  private MovementType movementType;

  /**
   * amount: crédito positivo, débito negativo (regla del enunciado).
   */
  @Column(name = "amount", nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  /**
   * balance: saldo disponible luego de aplicar el movimiento.
   */
  @Column(name = "balance", nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;
}
