package org.ikigaidigital.application.service.interest;

import org.springframework.stereotype.Component;

@Component
public final class InterestRateStrategyFactory {

  private final InterestRateStrategy basic = new BasicInterestRateStrategy();
  private final InterestRateStrategy student = new StudentInterestRateStrategy();
  private final InterestRateStrategy premium = new PremiumInterestRateStrategy();
  private final InterestRateStrategy noOp = new NoOpInterestRateStrategy();

  public InterestRateStrategy forPlanType(String planType) {
    return switch (planType) {
      case "basic" -> basic;
      case "student" -> student;
      case "premium" -> premium;
      default -> noOp;
    };
  }
}
