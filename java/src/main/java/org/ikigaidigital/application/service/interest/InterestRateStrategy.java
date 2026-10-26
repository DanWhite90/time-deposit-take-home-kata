package org.ikigaidigital.application.service.interest;

import org.ikigaidigital.domain.model.TimeDeposit;

public interface InterestRateStrategy {

  double interest(TimeDeposit deposit);
}
