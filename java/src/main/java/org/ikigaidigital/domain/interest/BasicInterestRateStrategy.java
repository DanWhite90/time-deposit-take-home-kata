package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.ikigaidigital.TimeDeposit;

final class BasicInterestRateStrategy implements InterestRateStrategy {

  private static final double ANNUAL_RATE = 0.01;

  private static final int MONTHS_PER_YEAR = 12;

  private static final int INTEREST_FREE_DAYS = 30;

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    if (deposit.getDays() > INTEREST_FREE_DAYS) {
      return new BigDecimal(deposit.getBalance() * ANNUAL_RATE / MONTHS_PER_YEAR).setScale(2, RoundingMode.HALF_UP);
    }

    return BigDecimal.ZERO;
  }
}
