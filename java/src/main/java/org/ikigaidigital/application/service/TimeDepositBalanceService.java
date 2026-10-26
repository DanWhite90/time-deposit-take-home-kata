package org.ikigaidigital.application.service;

import java.util.ArrayList;
import java.util.List;
import org.ikigaidigital.application.port.in.ListTimeDepositsUseCase;
import org.ikigaidigital.application.port.in.UpdateAllTimeDepositBalancesUseCase;
import org.ikigaidigital.application.port.in.UpdateTimeDepositBalanceUseCase;
import org.ikigaidigital.application.port.out.TimeDepositStore;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;
import org.springframework.stereotype.Service;

@Service
public class TimeDepositBalanceService implements ListTimeDepositsUseCase, UpdateAllTimeDepositBalancesUseCase {

  private final TimeDepositStore timeDepositStore;
  private final UpdateTimeDepositBalanceUseCase updateTimeDepositBalanceUseCase;

  public TimeDepositBalanceService(
      TimeDepositStore timeDepositStore,
      UpdateTimeDepositBalanceUseCase updateTimeDepositBalanceUseCase) {
    this.timeDepositStore = timeDepositStore;
    this.updateTimeDepositBalanceUseCase = updateTimeDepositBalanceUseCase;
  }

  @Override
  public List<TimeDepositWithWithdrawals> listTimeDeposits() {
    return timeDepositStore.findAll();
  }

  @Override
  public List<TimeDepositWithWithdrawals> updateAllBalances() {
    List<TimeDeposit> deposits = new ArrayList<>();
    for (TimeDepositWithWithdrawals account : timeDepositStore.findAll()) {
      deposits.add(account.toTimeDeposit());
    }
    updateTimeDepositBalanceUseCase.updateBalance(deposits);
    timeDepositStore.saveBalances(deposits);
    return timeDepositStore.findAll();
  }
}
