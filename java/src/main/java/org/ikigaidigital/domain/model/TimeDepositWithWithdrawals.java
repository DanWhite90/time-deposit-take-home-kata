package org.ikigaidigital.domain.model;

import static java.util.Collections.emptyList;

import java.math.BigDecimal;
import java.util.List;

public record TimeDepositWithWithdrawals(
    int id,
    String planType,
    BigDecimal balance,
    int days,
    List<Withdrawal> withdrawals) {

  public TimeDepositWithWithdrawals {
    withdrawals = withdrawals == null ? emptyList() : List.copyOf(withdrawals);
  }
}
