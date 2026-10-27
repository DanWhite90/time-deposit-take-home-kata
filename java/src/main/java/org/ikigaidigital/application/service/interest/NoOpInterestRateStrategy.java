package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.TimeDeposit;

final class NoOpInterestRateStrategy implements InterestRateStrategy {

  @Override
  public double interest(final TimeDeposit deposit) {
    return 0;
  }
}
