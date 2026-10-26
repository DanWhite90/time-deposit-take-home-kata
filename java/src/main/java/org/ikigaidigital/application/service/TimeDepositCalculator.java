package org.ikigaidigital.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.application.port.in.UpdateTimeDepositBalanceUseCase;
import org.ikigaidigital.application.service.interest.InterestRateStrategyFactory;
import org.ikigaidigital.domain.model.TimeDeposit;

@RequiredArgsConstructor
public class TimeDepositCalculator implements UpdateTimeDepositBalanceUseCase {

  private final InterestRateStrategyFactory interestRateStrategyFactory;

  @Override
  public void updateBalance(List<TimeDeposit> timeDeposits) {
    for (TimeDeposit timeDeposit : timeDeposits) {
      double interest = 0;

      // Plan type is read only after day 30, as in the original rules.
      if (timeDeposit.getDays() > 30) {
        interest += interestRateStrategyFactory.forPlanType(timeDeposit.getPlanType()).interest(timeDeposit);
      }

      double a2d = timeDeposit.getBalance() + (new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP)).doubleValue();
      timeDeposit.setBalance(a2d);
    }
  }
}
