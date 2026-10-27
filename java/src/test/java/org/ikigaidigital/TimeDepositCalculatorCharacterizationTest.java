package org.ikigaidigital;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TimeDepositCalculator characterization")
class TimeDepositCalculatorCharacterizationTest {

  private static final String[] PLAN_TYPES = {"basic", "student", "premium", "unknown"};

  private static final int DEPOSITS = 20_000;

  @Nested
  @DisplayName("updateBalance()")
  class UpdateBalance {

    @Test
    @DisplayName("produces the same balances as the original implementation")
    void matchesTheOriginalImplementation() {
      Random random = new Random(20260513L);
      List<TimeDeposit> refactored = new ArrayList<>();
      List<TimeDeposit> original = new ArrayList<>();

      for (int id = 0; id < DEPOSITS; id++) {
        String planType = PLAN_TYPES[random.nextInt(PLAN_TYPES.length)];
        int days = random.nextInt(800);
        double balance = randomBalance(random);

        refactored.add(new TimeDeposit(id, planType, balance, days));
        original.add(new TimeDeposit(id, planType, balance, days));
      }

      new TimeDepositCalculator().updateBalance(refactored);
      new OriginalTimeDepositCalculator().updateBalance(original);

      assertThat(refactored).extracting(TimeDeposit::getBalance)
          .containsExactlyElementsOf(original.stream().map(TimeDeposit::getBalance).toList());
    }

    /*
     * Mixes cent-precise balances with arbitrary doubles. The cent-precise ones include exact half-cent interest,
     * where the original's double arithmetic rounds inconsistently, so a switch to BigDecimal arithmetic fails here.
     */
    private static double randomBalance(final Random random) {
      double balance = random.nextDouble() * 2_000_000;

      return random.nextBoolean() ? Math.round(balance * 100) / 100.0 : balance;
    }
  }
}
