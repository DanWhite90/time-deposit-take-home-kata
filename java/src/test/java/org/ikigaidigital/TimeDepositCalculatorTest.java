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
      TimeDeposit deposit = new TimeDeposit(1, planType, balance, days);

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
        // The original's double arithmetic is kept on purpose (see README). Exact half-cent interest rounds up for
        // the basic balance but down for the student and premium ones.
        arguments("basic", 100, 138486.0, 138601.41),
        arguments("student", 100, 45402.0, 45515.5),
        arguments("student", 100, 177466.0, 177909.66),
        arguments("premium", 100, 1348194.0, 1353811.47),
        arguments("premium", 100, 473955.6, 475930.41),
        // The balance plus interest sum can be one binary digit off, as in the original.
        arguments("student", 100, 332763.74, 333595.64999999997),
        arguments("unknown", 100, 1200.0, 1200.0),
        arguments(null, 100, 1200.0, 1200.0));
    }
  }

}
