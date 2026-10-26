package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.domain.model.TimeDeposit;

final class NoOpInterestRateStrategy implements InterestRateStrategy {

  @Override
  public double interest(TimeDeposit deposit) {
    return 0;
  }
}
