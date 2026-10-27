package org.ikigaidigital.domain.model;

import static java.util.Collections.emptyList;

import java.math.BigDecimal;
import java.util.List;
import org.ikigaidigital.TimeDeposit;

public record TimeDepositWithWithdrawals(
    int id,
    String planType,
    BigDecimal balance,
    int days,
    List<Withdrawal> withdrawals) {

  public TimeDepositWithWithdrawals {
    withdrawals = withdrawals == null ? emptyList() : List.copyOf(withdrawals);
  }

  // The shared TimeDeposit model keeps its Double balance, so the conversion happens only at this boundary.
  public TimeDeposit toTimeDeposit() {
    return new TimeDeposit(id, planType, balance.doubleValue(), days);
  }
}
