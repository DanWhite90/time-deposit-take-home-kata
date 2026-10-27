package org.ikigaidigital;

import static org.ikigaidigital.domain.util.MoneyUtil.round;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.domain.interest.InterestRateStrategyFactory;

/**
 * Original kata entry point. Its package, public no-arg constructor and {@code updateBalance} signature stay unchanged
 * so existing callers keep compiling. The plan rules moved into interest rate strategies. It has no Spring dependency;
 * {@link org.ikigaidigital.config.DomainConfiguration} registers it as a bean.
 */
@RequiredArgsConstructor
public class TimeDepositCalculator {

  private final InterestRateStrategyFactory interestRateStrategyFactory;

  public TimeDepositCalculator() {
    this(new InterestRateStrategyFactory());
  }

  public void updateBalance(final List<TimeDeposit> timeDeposits) {
    for (TimeDeposit timeDeposit : timeDeposits) {
      BigDecimal interest = interestRateStrategyFactory.forPlanType(timeDeposit.getPlanType()).interest(timeDeposit);

      timeDeposit.setBalance(timeDeposit.getBalance() + round(interest).doubleValue());
    }
  }
}
