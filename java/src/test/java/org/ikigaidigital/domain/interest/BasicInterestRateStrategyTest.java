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

@DisplayName("BasicInterestRateStrategy")
class BasicInterestRateStrategyTest {

  private final InterestRateStrategy strategy = new BasicInterestRateStrategy();

  @Nested
  @DisplayName("interest()")
  class Interest {

    @ParameterizedTest(name = "at {0} days with balance {1} earns {2}")
    @MethodSource("cases")
    @DisplayName("provides correct interest calculations for given days and balance")
    void interest(final int days, final double balance, final String expectedInterest) {
      BigDecimal interest = strategy.interest(new TimeDeposit(1, "basic", balance, days));

      assertThat(interest).isEqualByComparingTo(expectedInterest);
    }

    private static Stream<Arguments> cases() {
      return Stream.of(
        arguments(0, 1200.0, "0.0"),
        arguments(30, 1200.0, "0.0"),
        arguments(31, 1200.0, "1.0"),
        arguments(45, 1234567.0, "1028.81"),
        arguments(365, 1200.0, "1.0"),
        arguments(366, 1200.0, "1.0"),
        arguments(45, 0.0, "0.0"));
    }
  }
}
