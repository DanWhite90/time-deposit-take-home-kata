package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.ikigaidigital.TimeDeposit;

final class StudentInterestRateStrategy implements InterestRateStrategy {

  private static final double ANNUAL_RATE = 0.03;

  private static final int MONTHS_PER_YEAR = 12;

  private static final int ONE_YEAR_IN_DAYS = 365;

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    if (deposit.getDays() <= ONE_YEAR_IN_DAYS) {
      return new BigDecimal(deposit.getBalance() * ANNUAL_RATE / MONTHS_PER_YEAR).setScale(2, RoundingMode.HALF_UP);
    }

    return BigDecimal.ZERO;
  }
}
