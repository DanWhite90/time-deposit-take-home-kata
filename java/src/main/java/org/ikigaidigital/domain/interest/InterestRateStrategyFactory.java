package org.ikigaidigital.domain.interest;

public final class InterestRateStrategyFactory {

  // The assignment: no interest is applied for the first 30 days for any existing plan.
  private static final int INTEREST_FREE_DAYS = 30;

  private final InterestRateStrategy basic = withSharedRules(new BasicInterestRateStrategy());
  private final InterestRateStrategy student = withSharedRules(new StudentInterestRateStrategy());
  private final InterestRateStrategy premium = withSharedRules(new PremiumInterestRateStrategy());
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

  // Rules that apply to every plan wrap the plan's own rules, so each of them lives in one place.
  private static InterestRateStrategy withSharedRules(final InterestRateStrategy plan) {
    return new InterestFreePeriod(INTEREST_FREE_DAYS, plan);
  }
}
