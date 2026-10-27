package org.ikigaidigital.domain.interest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.ikigaidigital.TimeDeposit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InterestFreePeriod")
class InterestFreePeriodTest {

  private static final BigDecimal PLAN_INTEREST = new BigDecimal("7.00");

  private final InterestRateStrategy strategy = new InterestFreePeriod(30, deposit -> PLAN_INTEREST);

  @Nested
  @DisplayName("interest()")
  class Interest {

    @ParameterizedTest(name = "at {0} days earns {1}")
    @MethodSource("cases")
    @DisplayName("pays nothing during the period and the plan's interest after it")
    void interest(final int days, final String expectedInterest) {
      BigDecimal interest = strategy.interest(new TimeDeposit(1, "basic", 1200.0, days));

      assertThat(interest).isEqualByComparingTo(expectedInterest);
    }

    private static Stream<Arguments> cases() {
      return Stream.of(
        arguments(0, "0"),
        arguments(30, "0"),
        arguments(31, "7.00"),
        arguments(366, "7.00"));
    }
  }
}
