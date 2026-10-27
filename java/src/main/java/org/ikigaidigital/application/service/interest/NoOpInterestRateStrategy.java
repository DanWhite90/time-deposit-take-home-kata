package org.ikigaidigital.application.service.interest;

import java.math.BigDecimal;
import org.ikigaidigital.TimeDeposit;

final class NoOpInterestRateStrategy implements InterestRateStrategy {

  @Override
  public BigDecimal interest(final TimeDeposit deposit) {
    return BigDecimal.ZERO;
  }
}
