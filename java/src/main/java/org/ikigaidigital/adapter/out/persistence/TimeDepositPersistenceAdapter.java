package org.ikigaidigital.adapter.out.persistence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.application.port.out.TimeDepositStore;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;
import org.ikigaidigital.domain.model.Withdrawal;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeDepositPersistenceAdapter implements TimeDepositStore {

  private final TimeDepositJpaRepository timeDeposits;

  @Override
  public List<TimeDepositWithWithdrawals> findAll() {
    return timeDeposits.findAllWithWithdrawals().stream().map(this::toDomain).toList();
  }

  @Override
  public void saveBalances(final List<TimeDeposit> deposits) {
    for (final TimeDeposit deposit : deposits) {
      timeDeposits.updateBalance(deposit.getId(), money(deposit.getBalance()));
    }
  }

  private TimeDepositWithWithdrawals toDomain(final TimeDepositEntity deposit) {
    final List<Withdrawal> withdrawals = deposit.getWithdrawals().stream()
        .map(withdrawal -> new Withdrawal(
            withdrawal.getId(),
            deposit.getId(),
            withdrawal.getAmount(),
            withdrawal.getDate()))
        .toList();

    return new TimeDepositWithWithdrawals(
        deposit.getId(),
        deposit.getPlanType(),
        deposit.getBalance(),
        deposit.getDays(),
        withdrawals);
  }

  private static BigDecimal money(final Double amount) {
    return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
  }
}
