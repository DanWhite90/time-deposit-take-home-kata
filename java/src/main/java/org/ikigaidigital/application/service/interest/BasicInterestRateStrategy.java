package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.TimeDeposit;

final class BasicInterestRateStrategy implements InterestRateStrategy {

  @Override
  public double interest(final TimeDeposit deposit) {
    double interest = 0;

    if (deposit.getDays() > 30) {
      interest = deposit.getBalance() * 0.01 / 12;
    }

    return interest;
  }
}
