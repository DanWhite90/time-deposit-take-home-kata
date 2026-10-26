package org.ikigaidigital.application.service.interest;

import static org.ikigaidigital.domain.util.MoneyUtil.round;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.application.port.in.UpdateTimeDepositBalanceUseCase;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeDepositCalculator implements UpdateTimeDepositBalanceUseCase {

  private final InterestRateStrategyFactory interestRateStrategyFactory;

  @Override
  public void updateBalance(final List<TimeDeposit> timeDeposits) {
    for (TimeDeposit timeDeposit : timeDeposits) {
      double interest = interestRateStrategyFactory.forPlanType(timeDeposit.getPlanType()).interest(timeDeposit);

      timeDeposit.setBalance(timeDeposit.getBalance() + round(interest));
    }
  }
}
