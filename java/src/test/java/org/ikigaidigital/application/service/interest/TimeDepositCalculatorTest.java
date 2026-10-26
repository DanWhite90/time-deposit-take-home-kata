package org.ikigaidigital.application.service.interest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TimeDepositCalculatorTest {

  private final TimeDepositCalculator calculator = new TimeDepositCalculator(new InterestRateStrategyFactory());

  @ParameterizedTest(name = "{0} at {1} days with balance {2} becomes {3}")
  @MethodSource("roundedBalances")
  void updateBalance_roundsEachStrategy(String planType, int days, double balance, double expectedBalance) {
    TimeDeposit deposit = new TimeDeposit(1, planType, balance, days);

    calculator.updateBalance(List.of(deposit));

    assertThat(deposit.getBalance()).isEqualTo(expectedBalance);
  }

  private static Stream<Arguments> roundedBalances() {
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
        arguments("unknown", 100, 1200.0, 1200.0));
  }

  @Test
  void updateBalance_readsPlanTypeOnlyAfterDay30() {
    TimeDeposit beforeInterestStarts = new TimeDeposit(1, null, 1000.0, 30);

    calculator.updateBalance(List.of(beforeInterestStarts));

    assertThat(beforeInterestStarts.getBalance()).isEqualTo(1000.0);
    assertThatThrownBy(() -> calculator.updateBalance(
        List.of(new TimeDeposit(2, null, 1000.0, 31))))
        .isInstanceOf(NullPointerException.class);
  }
}
