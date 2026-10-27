package org.ikigaidigital;

import static org.ikigaidigital.domain.util.MoneyUtil.round;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.application.service.interest.InterestRateStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Original kata entry point. Its package, public no-arg constructor and {@code updateBalance} signature stay
 * unchanged so existing callers keep compiling. The plan rules moved into interest rate strategies.
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TimeDepositCalculator {

  private final InterestRateStrategyFactory interestRateStrategyFactory;

  public TimeDepositCalculator() {
    this(new InterestRateStrategyFactory());
  }

  public void updateBalance(final List<TimeDeposit> xs) {
    for (final TimeDeposit timeDeposit : xs) {
      final double interest = interestRateStrategyFactory.forPlanType(timeDeposit.getPlanType()).interest(timeDeposit);

      timeDeposit.setBalance(timeDeposit.getBalance() + round(interest));
    }
  }
}
