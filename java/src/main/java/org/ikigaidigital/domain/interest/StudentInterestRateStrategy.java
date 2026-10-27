package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import java.math.MathContext;
import org.ikigaidigital.TimeDeposit;

final class StudentInterestRateStrategy implements InterestRateStrategy {

  private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.03").divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    if (deposit.getDays() > 30 && deposit.getDays() < 366) {
      return BigDecimal.valueOf(deposit.getBalance()).multiply(MONTHLY_RATE, MathContext.DECIMAL64);
    }

    return BigDecimal.ZERO;
  }
}
