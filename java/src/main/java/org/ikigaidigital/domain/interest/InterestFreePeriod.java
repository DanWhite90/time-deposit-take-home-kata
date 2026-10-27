package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import org.ikigaidigital.TimeDeposit;

/**
 * Pays no interest during a deposit's first {@code days}, whatever the plan. The factory wraps every plan in it, so the
 * rule that applies to all plans lives in one place; a plan can still start later, as premium does after 45 days.
 */
record InterestFreePeriod(int days, InterestRateStrategy plan) implements InterestRateStrategy {

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    if (deposit.getDays() > days) {
      return plan.interest(deposit);
    }

    return BigDecimal.ZERO;
  }
}
