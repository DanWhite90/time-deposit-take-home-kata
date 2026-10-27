package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.TimeDeposit;

public interface InterestRateStrategy {

  double interest(TimeDeposit deposit);
}
