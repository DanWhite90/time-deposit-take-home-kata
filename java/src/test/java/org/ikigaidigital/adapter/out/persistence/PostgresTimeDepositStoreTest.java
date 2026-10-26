package org.ikigaidigital.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.ikigaidigital.PostgresTestcontainersConfiguration;
import org.ikigaidigital.application.port.out.TimeDepositStore;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PostgresTestcontainersConfiguration.class)
class PostgresTimeDepositStoreTest {

  @Autowired
  private TimeDepositStore timeDepositStore;

  @Autowired
  private TimeDepositJpaRepository timeDeposits;

  @Autowired
  private WithdrawalJpaRepository withdrawals;

  @BeforeEach
  void clearTables() {
    withdrawals.deleteAll();
    timeDeposits.deleteAll();
  }

  @Test
  void findAllReturnsPlansWithTheirWithdrawals() {
    TimeDepositEntity deposit = timeDeposits.save(
        new TimeDepositEntity(1, "student", 31, new BigDecimal("1200.00")));
    withdrawals.save(new WithdrawalEntity(4, deposit, new BigDecimal("10.00"), LocalDate.of(2026, 3, 1)));

    assertThat(timeDepositStore.findAll()).singleElement().satisfies(loaded -> {
      assertThat(loaded.id()).isEqualTo(1);
      assertThat(loaded.planType()).isEqualTo("student");
      assertThat(loaded.balance()).isEqualTo(1200.0);
      assertThat(loaded.days()).isEqualTo(31);
      assertThat(loaded.withdrawals()).singleElement().satisfies(withdrawal -> {
        assertThat(withdrawal.id()).isEqualTo(4);
        assertThat(withdrawal.timeDepositId()).isEqualTo(1);
        assertThat(withdrawal.amount()).isEqualTo(10.0);
        assertThat(withdrawal.date()).isEqualTo(LocalDate.of(2026, 3, 1));
      });
    });
  }

  @Test
  void saveBalancesUpdatesTheBalanceAndLeavesTheWithdrawal() {
    TimeDepositEntity deposit = timeDeposits.save(
        new TimeDepositEntity(1, "basic", 45, new BigDecimal("1000.00")));
    withdrawals.save(new WithdrawalEntity(4, deposit, new BigDecimal("10.00"), LocalDate.of(2026, 3, 1)));

    timeDepositStore.saveBalances(List.of(new TimeDeposit(1, "basic", 1001.0, 45)));

    assertThat(timeDepositStore.findAll()).singleElement().satisfies(loaded -> {
      assertThat(loaded.balance()).isEqualTo(1001.0);
      assertThat(loaded.days()).isEqualTo(45);
      assertThat(loaded.withdrawals()).singleElement().satisfies(withdrawal -> {
        assertThat(withdrawal.amount()).isEqualTo(10.0);
        assertThat(withdrawal.date()).isEqualTo(LocalDate.of(2026, 3, 1));
      });
    });
  }
}
