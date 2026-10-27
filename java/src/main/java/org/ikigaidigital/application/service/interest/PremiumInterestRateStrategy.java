package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.TimeDeposit;

final class PremiumInterestRateStrategy implements InterestRateStrategy {

  @Override
  public double interest(final TimeDeposit deposit) {
    double interest = 0;

    if (deposit.getDays() > 45) {
      interest = deposit.getBalance() * 0.05 / 12;
    }

    return interest;
  }
}
