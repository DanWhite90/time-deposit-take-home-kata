package org.ikigaidigital.domain.interest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.ikigaidigital.TimeDeposit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("InterestRateStrategyFactory")
class InterestRateStrategyFactoryTest {

  private static final double BALANCE = 1200.0;

  private final InterestRateStrategyFactory factory = new InterestRateStrategyFactory();

  @Nested
  @DisplayName("forPlanType()")
  class ForPlanType {

    @Test
    @DisplayName("returns one singleton per plan type")
    void returnsOneSingletonPerPlanType() {
      assertThat(factory.forPlanType("basic")).isSameAs(factory.forPlanType("basic"));
      assertThat(factory.forPlanType("student")).isSameAs(factory.forPlanType("student"));
      assertThat(factory.forPlanType("premium")).isSameAs(factory.forPlanType("premium"));
      assertThat(factory.forPlanType("unknown")).isSameAs(factory.forPlanType("other"));

      assertThat(factory.forPlanType("basic"))
          .isNotSameAs(factory.forPlanType("student"))
          .isNotSameAs(factory.forPlanType("premium"))
          .isNotSameAs(factory.forPlanType("unknown"));
    }

    @ParameterizedTest(name = "{0} earns {1} a month on 1200")
    @MethodSource("monthlyInterestOn1200")
    @DisplayName("applies each plan's own rate")
    void appliesEachPlansOwnRate(final String planType, final String expectedInterest) {
      BigDecimal interest = factory.forPlanType(planType).interest(deposit(planType, 100));

      assertThat(interest).isEqualByComparingTo(expectedInterest);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"basic", "student", "premium"})
    @DisplayName("gives every plan the 30-day interest-free period")
    void givesEveryPlanTheInterestFreePeriod(final String planType) {
      assertThat(factory.forPlanType(planType).interest(deposit(planType, 30))).isZero();
    }

    @ParameterizedTest(name = "unrecognized plan type \"{0}\"")
    @MethodSource("unrecognizedPlanTypes")
    @DisplayName("uses the no-op singleton for an unrecognized plan type")
    void unrecognizedPlanTypesUseTheNoOpSingleton(final String planType) {
      assertThat(factory.forPlanType(planType))
          .isSameAs(factory.forPlanType("unknown"))
          .isInstanceOf(NoOpInterestRateStrategy.class);
    }

    private static Stream<Arguments> monthlyInterestOn1200() {
      return Stream.of(
          arguments("basic", "1.00"),
          arguments("student", "3.00"),
          arguments("premium", "5.00"));
    }

    private static Stream<Arguments> unrecognizedPlanTypes() {
      return Stream.of(
          arguments("unknown"),
          arguments("Basic"),
          arguments("STUDENT"),
          arguments("Premium"),
          arguments("premium "),
          arguments("student "));
    }

    private static TimeDeposit deposit(final String planType, final int days) {
      return new TimeDeposit(1, planType, BALANCE, days);
    }
  }
}
