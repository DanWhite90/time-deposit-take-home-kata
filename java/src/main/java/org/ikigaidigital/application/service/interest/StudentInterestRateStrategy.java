package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.TimeDeposit;

final class StudentInterestRateStrategy implements InterestRateStrategy {

  @Override
  public double interest(final TimeDeposit deposit) {
    double interest = 0;

    if (deposit.getDays() > 30 && deposit.getDays() < 366) {
      interest = deposit.getBalance() * 0.03 / 12;
    }

    return interest;
  }
}
