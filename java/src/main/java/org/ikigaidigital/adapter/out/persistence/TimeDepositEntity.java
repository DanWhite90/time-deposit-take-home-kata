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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Persistent fields are not final: JPA forbids it and newer JDKs block reflective writes to final fields.
@Entity
@Table(name = "timeDeposits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
