package org.ikigaidigital.domain.model;

import static java.util.Collections.emptyList;

import java.util.List;

public record TimeDepositWithWithdrawals(
    int id,
    String planType,
    Double balance,
    int days,
    List<Withdrawal> withdrawals) {

  public TimeDepositWithWithdrawals {
    withdrawals = withdrawals == null ? emptyList() : List.copyOf(withdrawals);
  }

  public TimeDeposit toTimeDeposit() {
    return new TimeDeposit(id, planType, balance, days);
  }

  public TimeDepositWithWithdrawals withBalance(Double updatedBalance) {
    return new TimeDepositWithWithdrawals(id, planType, updatedBalance, days, withdrawals);
  }
}
