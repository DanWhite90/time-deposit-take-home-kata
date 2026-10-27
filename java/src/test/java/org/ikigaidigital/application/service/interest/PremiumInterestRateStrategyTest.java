package org.ikigaidigital.application.service.interest;

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

@DisplayName("PremiumInterestRateStrategy")
class PremiumInterestRateStrategyTest {

  private final InterestRateStrategy strategy = new PremiumInterestRateStrategy();

  @Nested
  @DisplayName("interest()")
  class Interest {

    @ParameterizedTest(name = "at {0} days with balance {1} earns {2}")
    @MethodSource("cases")
    @DisplayName("provides correct interest calculations for given days and balance")
    void interest(final int days, final double balance, final String expectedInterest) {
      final BigDecimal interest = strategy.interest(new TimeDeposit(1, "premium", balance, days));

      assertThat(interest).isEqualByComparingTo(expectedInterest);
    }

    private static Stream<Arguments> cases() {
      return Stream.of(
        arguments(0, 1200.0, "0.0"),
        arguments(30, 1200.0, "0.0"),
        arguments(31, 1200.0, "0.0"),
        arguments(45, 1200.0, "0.0"),
        arguments(46, 1200.0, "5.0"),
        arguments(365, 1200.0, "5.0"),
        arguments(366, 1200.0, "5.0"),
        arguments(100, 0.0, "0.0"));
    }
  }
}
