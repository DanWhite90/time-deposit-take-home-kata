package org.ikigaidigital;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TimeDepositCalculator characterization")
class TimeDepositCalculatorCharacterizationTest {

  private static final String[] PLAN_TYPES = {"basic", "student", "premium", "unknown"};

  private static final Map<String, BigDecimal> ANNUAL_RATES = Map.of(
      "basic", new BigDecimal("0.01"),
      "student", new BigDecimal("0.03"),
      "premium", new BigDecimal("0.05"));

  private static final BigDecimal MONTHS_PER_YEAR = BigDecimal.valueOf(12);

  private static final BigDecimal HALF_CENT = new BigDecimal("0.005");

  private static final BigDecimal CENT = new BigDecimal("0.01");

  private static final int DEPOSITS = 20_000;

  @Nested
  @DisplayName("updateBalance()")
  class UpdateBalance {

    @Test
    @DisplayName("produces the same balances as the original implementation, except on exact half-cent interest")
    void matchesTheOriginalImplementation() {
      final Random random = new Random(20260513L);
      final List<TimeDeposit> refactored = new ArrayList<>();
      final List<TimeDeposit> original = new ArrayList<>();
      final List<Double> startingBalances = new ArrayList<>();

      for (int id = 0; id < DEPOSITS; id++) {
        final String planType = PLAN_TYPES[random.nextInt(PLAN_TYPES.length)];
        final int days = random.nextInt(800);
        final double balance = randomBalance(random);

        refactored.add(new TimeDeposit(id, planType, balance, days));
        original.add(new TimeDeposit(id, planType, balance, days));
        startingBalances.add(balance);
      }

      new TimeDepositCalculator().updateBalance(refactored);
      new OriginalTimeDepositCalculator().updateBalance(original);

      for (int i = 0; i < DEPOSITS; i++) {
        final TimeDeposit deposit = refactored.get(i);

        if (deposit.getBalance().equals(original.get(i).getBalance())) {
          continue;
        }

        assertCorrectHalfCentRounding(deposit, startingBalances.get(i));
      }
    }

    /*
     * The original computes interest in double, so an exact half-cent such as 113.505 may become 113.50499... and
     * round down. The refactored calculator uses BigDecimal and rounds it up. Only those ties may differ.
     */
    private static void assertCorrectHalfCentRounding(final TimeDeposit deposit, final double startingBalance) {
      final BigDecimal exactInterest = BigDecimal.valueOf(startingBalance)
          .multiply(ANNUAL_RATES.get(deposit.getPlanType()))
          .divide(MONTHS_PER_YEAR, 20, RoundingMode.HALF_UP);
      final double expectedBalance = startingBalance + exactInterest.setScale(2, RoundingMode.HALF_UP).doubleValue();

      assertThat(exactInterest.remainder(CENT)).as("interest of deposit %d is an exact half-cent", deposit.getId())
          .isEqualByComparingTo(HALF_CENT);
      assertThat(deposit.getBalance()).as("balance of deposit %d", deposit.getId()).isEqualTo(expectedBalance);
    }

    // Mixes cent-precise balances with arbitrary doubles to exercise the rounding edge cases.
    private static double randomBalance(final Random random) {
      final double balance = random.nextDouble() * 2_000_000;

      return random.nextBoolean() ? Math.round(balance * 100) / 100.0 : balance;
    }
  }
}
