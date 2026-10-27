package org.ikigaidigital.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.PostgresTestcontainersConfiguration;
import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.TimeDepositTestData;
import org.ikigaidigital.application.port.out.TimeDepositStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@Import({PostgresTestcontainersConfiguration.class, TimeDepositTestData.class})
@DisplayName("TimeDepositPersistenceAdapter")
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class TimeDepositPersistenceAdapterTest {

  private final TimeDepositStore timeDepositStore;

  private final TimeDepositTestData testData;

  private final TransactionTemplate transactionTemplate;

  @BeforeEach
  void clearTables() {
    testData.clear();
  }

  @Nested
  @DisplayName("findAll()")
  class FindAll {

    @Test
    @DisplayName("returns plans with their withdrawals")
    void findAllReturnsPlansWithTheirWithdrawals() {
      testData.insertTimeDeposit(1, "student", 31, "1200.00");
      testData.insertWithdrawal(4, 1, "10.00", LocalDate.of(2026, 3, 1));

      assertThat(timeDepositStore.findAll()).singleElement().satisfies(loaded -> {
        assertThat(loaded.id()).isEqualTo(1);
        assertThat(loaded.planType()).isEqualTo("student");
        assertThat(loaded.balance()).isEqualByComparingTo("1200.00");
        assertThat(loaded.days()).isEqualTo(31);
        assertThat(loaded.withdrawals()).singleElement().satisfies(withdrawal -> {
          assertThat(withdrawal.id()).isEqualTo(4);
          assertThat(withdrawal.timeDepositId()).isEqualTo(1);
          assertThat(withdrawal.amount()).isEqualByComparingTo("10.00");
          assertThat(withdrawal.date()).isEqualTo(LocalDate.of(2026, 3, 1));
        });
      });
    }
  }

  @Nested
  @DisplayName("findAllTimeDeposits()")
  class FindAllTimeDeposits {

    @Test
    @DisplayName("returns plans ordered by id as the calculator model")
    void findAllTimeDepositsReturnsPlansOrderedById() {
      testData.insertTimeDeposit(2, "premium", 46, "1200.00");
      testData.insertTimeDeposit(1, "basic", 31, "999.99");
      testData.insertWithdrawal(4, 2, "10.00", LocalDate.of(2026, 3, 1));

      assertThat(timeDepositStore.findAllTimeDeposits())
          .extracting(TimeDeposit::getId, TimeDeposit::getPlanType, TimeDeposit::getBalance, TimeDeposit::getDays)
          .containsExactly(tuple(1, "basic", 999.99, 31), tuple(2, "premium", 1200.0, 46));
    }
  }

  @Nested
  @DisplayName("saveBalances()")
  class SaveBalances {

    @Test
    @DisplayName("updates the balance and leaves the withdrawal")
    void saveBalancesUpdatesTheBalanceAndLeavesTheWithdrawal() {
      testData.insertTimeDeposit(1, "basic", 45, "1000.00");
      testData.insertWithdrawal(4, 1, "10.00", LocalDate.of(2026, 3, 1));

      // In production the store joins the service transaction; the template plays that role here.
      transactionTemplate.executeWithoutResult(
          status -> timeDepositStore.saveBalances(List.of(new TimeDeposit(1, "basic", 1001.0, 45))));

      assertThat(timeDepositStore.findAll()).singleElement().satisfies(loaded -> {
        assertThat(loaded.balance()).isEqualByComparingTo("1001.00");
        assertThat(loaded.days()).isEqualTo(45);
        assertThat(loaded.withdrawals()).singleElement().satisfies(withdrawal -> {
          assertThat(withdrawal.amount()).isEqualByComparingTo("10.00");
          assertThat(withdrawal.date()).isEqualTo(LocalDate.of(2026, 3, 1));
        });
      });
    }
  }
}
