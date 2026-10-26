package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.domain.model.TimeDeposit;

final class StudentInterestRateStrategy implements InterestRateStrategy {

  @Override
  public double interest(TimeDeposit deposit) {
    double interest = 0;
    if (deposit.getDays() > 30) {
      if (deposit.getDays() < 366) {
        interest += deposit.getBalance() * 0.03 / 12;
      }
    }
    return interest;
  }
}
