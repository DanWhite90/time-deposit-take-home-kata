package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.domain.model.TimeDeposit;

final class BasicInterestRateStrategy implements InterestRateStrategy {

  @Override
  public double interest(TimeDeposit deposit) {
    double interest = 0;
    if (deposit.getDays() > 30) {
      interest += deposit.getBalance() * 0.01 / 12;
    }
    return interest;
  }
}
