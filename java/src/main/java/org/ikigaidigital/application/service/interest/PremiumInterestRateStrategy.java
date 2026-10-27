package org.ikigaidigital.application.service.interest;

import java.math.BigDecimal;
import java.math.MathContext;
import org.ikigaidigital.TimeDeposit;

final class PremiumInterestRateStrategy implements InterestRateStrategy {

  private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.05").divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    if (deposit.getDays() > 45) {
      return BigDecimal.valueOf(deposit.getBalance()).multiply(MONTHLY_RATE, MathContext.DECIMAL64);
    }

    return BigDecimal.ZERO;
  }
}
