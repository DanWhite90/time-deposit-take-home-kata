package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.ikigaidigital.TimeDeposit;

final class StudentInterestRateStrategy implements InterestRateStrategy {

  private static final double ANNUAL_RATE = 0.03;

  private static final int MONTHS_PER_YEAR = 12;

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    if (deposit.getDays() > 30 && deposit.getDays() < 366) {
      return new BigDecimal(deposit.getBalance() * ANNUAL_RATE / MONTHS_PER_YEAR).setScale(2, RoundingMode.HALF_UP);
    }

    return BigDecimal.ZERO;
  }
}
