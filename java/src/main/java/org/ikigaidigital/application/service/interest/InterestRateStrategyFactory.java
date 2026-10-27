package org.ikigaidigital.application.service.interest;

import org.springframework.stereotype.Component;

@Component
public final class InterestRateStrategyFactory {

  private final InterestRateStrategy basic = new BasicInterestRateStrategy();
  private final InterestRateStrategy student = new StudentInterestRateStrategy();
  private final InterestRateStrategy premium = new PremiumInterestRateStrategy();
  private final InterestRateStrategy noOp = new NoOpInterestRateStrategy();

  public InterestRateStrategy forPlanType(final String planType) {
    return switch (planType) {
      case "basic" -> basic;
      case "student" -> student;
      case "premium" -> premium;
      // Assumption: unknown plans earn no interest, as in the original. The original threw a
      // NullPointerException for a null plan after 30 days; invalid input is out of scope, so null earns none too.
      case null, default -> noOp;
    };
  }
}
