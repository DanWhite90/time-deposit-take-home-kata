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
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "timeDeposits")
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class TimeDepositEntity {

  @Id
  private final Integer id;

  @Column(name = "planType", nullable = false)
  private final String planType;

  @Column(nullable = false)
  private final Integer days;

  @Column(nullable = false, precision = 19, scale = 2)
  private final BigDecimal balance;

  @OneToMany(mappedBy = "timeDeposit")
  @OrderBy("id")
  private List<WithdrawalEntity> withdrawals = new ArrayList<>();
}
