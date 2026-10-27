package org.ikigaidigital.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.TimeDepositCalculator;
import org.ikigaidigital.application.port.out.TimeDepositStore;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

@DisplayName("TimeDepositService")
class TimeDepositServiceTest {

  private final TimeDepositStore timeDepositStore = mock(TimeDepositStore.class);

  private final TimeDepositService service = new TimeDepositService(timeDepositStore, new TimeDepositCalculator());

  private final List<TimeDepositWithWithdrawals> stored = List.of(
      new TimeDepositWithWithdrawals(1, "basic", new BigDecimal("1201.00"), 31, List.of()));

  @Nested
  @DisplayName("listTimeDeposits()")
  class ListTimeDeposits {

    @Test
    @DisplayName("returns the stored plans with their withdrawals")
    void listTimeDepositsReturnsTheStoredPlans() {
      when(timeDepositStore.findAll()).thenReturn(stored);

      assertThat(service.listTimeDeposits()).isEqualTo(stored);
      verify(timeDepositStore).findAll();
      verifyNoMoreInteractions(timeDepositStore);
    }
  }

  @Nested
  @DisplayName("updateAllBalances()")
  class UpdateAllBalances {

    @Test
    @DisplayName("reads the plans without withdrawals, saves the new balances, then returns the reloaded plans")
    void updateAllBalancesSavesTheCalculatedBalancesAndReloads() {
      TimeDeposit deposit = new TimeDeposit(1, "basic", 1200.0, 31);

      when(timeDepositStore.findAllTimeDeposits()).thenReturn(List.of(deposit));
      when(timeDepositStore.findAll()).thenReturn(stored);

      List<TimeDepositWithWithdrawals> result = service.updateAllBalances();

      InOrder order = inOrder(timeDepositStore);

      order.verify(timeDepositStore).findAllTimeDeposits();
      order.verify(timeDepositStore).saveBalances(List.of(deposit));
      order.verify(timeDepositStore).findAll();
      verifyNoMoreInteractions(timeDepositStore);
      assertThat(deposit.getBalance()).isEqualTo(1201.0);
      assertThat(result).isEqualTo(stored);
    }

    @Test
    @DisplayName("saves an empty list when there are no plans")
    void updateAllBalancesHandlesNoPlans() {
      when(timeDepositStore.findAllTimeDeposits()).thenReturn(List.of());
      when(timeDepositStore.findAll()).thenReturn(List.of());

      assertThat(service.updateAllBalances()).isEmpty();
      verify(timeDepositStore).saveBalances(anyList());
    }
  }
}
