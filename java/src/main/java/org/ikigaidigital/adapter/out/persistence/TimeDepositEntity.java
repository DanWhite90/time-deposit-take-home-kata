package org.ikigaidigital.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timeDeposits")
@Getter
@NoArgsConstructor
public class TimeDepositEntity {

  @Id
  private Integer id;

  @Column(name = "planType", nullable = false)
  private String planType;

  @Column(nullable = false)
  private Integer days;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal balance;

  @OneToMany(mappedBy = "timeDeposit")
  @OrderBy("id")
  private List<WithdrawalEntity> withdrawals = new ArrayList<>();

  public TimeDepositEntity(Integer id, String planType, Integer days, BigDecimal balance) {
    this.id = id;
    this.planType = planType;
    this.days = days;
    this.balance = balance;
  }
}
