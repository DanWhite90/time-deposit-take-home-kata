package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.ikigaidigital.TimeDeposit;

final class StudentInterestRateStrategy implements InterestRateStrategy {

  private static final BigDecimal ANNUAL_RATE = new BigDecimal("0.03");

  private static final BigDecimal MONTHS_PER_YEAR = BigDecimal.valueOf(12);

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    if (deposit.getDays() > 30 && deposit.getDays() < 366) {
      return BigDecimal.valueOf(deposit.getBalance())
          .multiply(ANNUAL_RATE)
          .divide(MONTHS_PER_YEAR, 2, RoundingMode.HALF_UP);
    }

    return BigDecimal.ZERO;
  }
}
