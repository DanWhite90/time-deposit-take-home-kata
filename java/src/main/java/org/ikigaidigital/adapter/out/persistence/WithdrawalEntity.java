package org.ikigaidigital.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "withdrawals")
@Getter
@NoArgsConstructor
public class WithdrawalEntity {

  @Id
  private Integer id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "timeDepositId", nullable = false)
  private TimeDepositEntity timeDeposit;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false)
  private LocalDate date;

  public WithdrawalEntity(Integer id, TimeDepositEntity timeDeposit, BigDecimal amount, LocalDate date) {
    this.id = id;
    this.timeDeposit = timeDeposit;
    this.amount = amount;
    this.date = date;
  }
}
