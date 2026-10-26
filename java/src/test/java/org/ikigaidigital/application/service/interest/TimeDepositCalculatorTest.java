package org.ikigaidigital.application.service.interest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("TimeDepositCalculator")
public class TimeDepositCalculatorTest {

  private final TimeDepositCalculator calculator = new TimeDepositCalculator(new InterestRateStrategyFactory());

  @Nested
  @DisplayName("updateBalance()")
  class UpdateBalance {

    @ParameterizedTest(name = "{0} at {1} days with balance {2} becomes {3}")
    @MethodSource("provideInputDepositsAndExpectedBalances")
    @DisplayName("Calculates correct new balance for given plan type and days")
    void updateBalance_roundsEachStrategy(String planType, int days, double balance, double expectedBalance) {
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
        arguments("unknown", 100, 1200.0, 1200.0),
        arguments(null, 100, 1200.0, 1200.0));
    }
  }

}
