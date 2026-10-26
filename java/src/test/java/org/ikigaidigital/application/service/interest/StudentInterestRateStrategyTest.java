package org.ikigaidigital.application.service.interest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StudentInterestRateStrategyTest {

  private final InterestRateStrategy strategy = new StudentInterestRateStrategy();

  @ParameterizedTest(name = "at {0} days with balance {1}, earns interest: {2}")
  @MethodSource("cases")
  void interest(int days, double balance, boolean earnsInterest) {
    double interest = strategy.interest(new TimeDeposit(1, "student", balance, days));

    assertThat(interest).isEqualTo(earnsInterest ? balance * 0.03 / 12 : 0d);
  }

  private static Stream<Arguments> cases() {
    return Stream.of(
        arguments(0, 1200.0, false),
        arguments(30, 1200.0, false),
        arguments(31, 1200.0, true),
        arguments(45, 1200.0, true),
        arguments(365, 1200.0, true),
        arguments(366, 1200.0, false),
        arguments(400, 5000.0, false),
        arguments(100, 0.0, true));
  }
}
