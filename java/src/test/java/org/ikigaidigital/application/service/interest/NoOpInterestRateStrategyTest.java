package org.ikigaidigital.application.service.interest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("NoOpInterestRateStrategy")
class NoOpInterestRateStrategyTest {

  private final InterestRateStrategy strategy = new NoOpInterestRateStrategy();

  @ParameterizedTest(name = "at {0} days with balance {1}")
  @MethodSource("cases")
  @DisplayName("provides correct interest calculations for given days and balance")
  void interest(int days, double balance) {
    double interest = strategy.interest(new TimeDeposit(1, "unknown", balance, days));

    assertThat(interest).isZero();
  }

  private static Stream<Arguments> cases() {
    return Stream.of(
        arguments(0, 0.0),
        arguments(30, 1200.0),
        arguments(31, 1200.0),
        arguments(45, 999.99),
        arguments(46, 1200.0),
        arguments(365, 1200.0),
        arguments(366, 1200.0),
        arguments(1000, 5000.0));
  }
}
