package org.ikigaidigital;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("TimeDepositCalculator")
class TimeDepositCalculatorTest {

  private final TimeDepositCalculator calculator = new TimeDepositCalculator();

  @Nested
  @DisplayName("updateBalance()")
  class UpdateBalance {

    @ParameterizedTest(name = "{0} at {1} days with balance {2} becomes {3}")
    @MethodSource("provideInputDepositsAndExpectedBalances")
    @DisplayName("Calculates correct new balance for given plan type and days")
    void updateBalanceRoundsEachStrategy(
        final String planType, final int days, final double balance, final double expectedBalance) {
      final TimeDeposit deposit = new TimeDeposit(1, planType, balance, days);

      calculator.updateBalance(List.of(deposit));

      assertThat(deposit.getBalance()).isEqualTo(expectedBalance);
    }

    private static Stream<Arguments> provideInputDepositsAndExpectedBalances() {
      return Stream.of(
        arguments("basic", 30, 1200.0, 1200.0),
        arguments("basic", 31, 1200.0, 1201.0),
        arguments("basic", 45, 1234567.0, 1235595.81),
        arguments("basic", 366, 1200.0, 1201.0),
        arguments("student", 30, 1200.0, 1200.0),
        arguments("student", 31, 1200.0, 1203.0),
        arguments("student", 365, 1200.0, 1203.0),
        arguments("student", 366, 1200.0, 1200.0),
        arguments("premium", 31, 1200.0, 1200.0),
        arguments("premium", 45, 1200.0, 1200.0),
        arguments("premium", 46, 1200.0, 1205.0),
        arguments("premium", 366, 1200.0, 1205.0),
        // Exact half-cent interest rounds half up, despite the truncated monthly rate. The original double
        // arithmetic rounded the student and premium cases down.
        arguments("basic", 100, 138486.0, 138601.41),
        arguments("student", 100, 45402.0, 45515.51),
        arguments("student", 100, 177466.0, 177909.67),
        arguments("premium", 100, 1348194.0, 1353811.48),
        arguments("premium", 100, 473955.6, 475930.42),
        arguments("unknown", 100, 1200.0, 1200.0),
        arguments(null, 100, 1200.0, 1200.0));
    }
  }

}
