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
    @DisplayName("matches the original except where its double arithmetic was inexact")
    void matchesTheOriginalImplementation() {
      Random random = new Random(20260513L);
      List<TimeDeposit> refactored = new ArrayList<>();
      List<TimeDeposit> original = new ArrayList<>();
      List<Double> startingBalances = new ArrayList<>();

      for (int id = 0; id < DEPOSITS; id++) {
        String planType = PLAN_TYPES[random.nextInt(PLAN_TYPES.length)];
        int days = random.nextInt(800);
        double balance = randomBalance(random);

        refactored.add(new TimeDeposit(id, planType, balance, days));
        original.add(new TimeDeposit(id, planType, balance, days));
        startingBalances.add(balance);
      }

      new TimeDepositCalculator().updateBalance(refactored);
      new OriginalTimeDepositCalculator().updateBalance(original);

      for (int i = 0; i < DEPOSITS; i++) {
        TimeDeposit deposit = refactored.get(i);

        if (deposit.getBalance().equals(original.get(i).getBalance())) {
          continue;
        }

        assertMoreAccurateThanTheOriginal(deposit, original.get(i), startingBalances.get(i));
      }
    }

    /*
     * The refactored calculator works in BigDecimal, so it may differ from the original only where the original's
     * double arithmetic was inexact:
     * - exact half-cent interest such as 113.505, which the original stored as 113.50499... and rounded down;
     * - the balance plus interest addition, where the original's double sum can be one binary digit off the exact
     *   decimal result, e.g. 333595.64999999997 instead of 333595.65.
     * In both cases the refactored balance must be the double closest to the exact decimal result.
     */
    private static void assertMoreAccurateThanTheOriginal(
        final TimeDeposit deposit, final TimeDeposit original, final double startingBalance) {
      BigDecimal exactInterest = BigDecimal.valueOf(startingBalance)
          .multiply(ANNUAL_RATES.get(deposit.getPlanType()))
          .divide(MONTHS_PER_YEAR, 20, RoundingMode.HALF_UP);
      double expectedBalance = BigDecimal.valueOf(startingBalance)
          .add(exactInterest.setScale(2, RoundingMode.HALF_UP))
          .doubleValue();
      boolean halfCentTie = exactInterest.remainder(CENT).compareTo(HALF_CENT) == 0;
      boolean oneBinaryDigitOff = Math.abs(deposit.getBalance() - original.getBalance()) <= Math.ulp(expectedBalance);

      assertThat(deposit.getBalance()).as("balance of deposit %d", deposit.getId()).isEqualTo(expectedBalance);
      assertThat(halfCentTie || oneBinaryDigitOff)
          .as("deposit %d differs from the original only by a half-cent tie or one binary digit", deposit.getId())
          .isTrue();
    }

    // Mixes cent-precise balances with arbitrary doubles to exercise the rounding edge cases.
    private static double randomBalance(final Random random) {
      double balance = random.nextDouble() * 2_000_000;

      return random.nextBoolean() ? Math.round(balance * 100) / 100.0 : balance;
    }
  }
}
