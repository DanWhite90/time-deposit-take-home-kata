package org.ikigaidigital.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.TimeDepositCalculator;
import org.ikigaidigital.application.port.in.ListTimeDepositsUseCase;
import org.ikigaidigital.application.port.in.UpdateAllTimeDepositBalancesUseCase;
import org.ikigaidigital.application.port.out.TimeDepositStore;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimeDepositService implements ListTimeDepositsUseCase, UpdateAllTimeDepositBalancesUseCase {

  private final TimeDepositStore timeDepositStore;

  private final TimeDepositCalculator timeDepositCalculator;

  @Override
  @Transactional(readOnly = true)
  public List<TimeDepositWithWithdrawals> listTimeDeposits() {
    return timeDepositStore.findAll();
  }

  @Override
  @Transactional
  public List<TimeDepositWithWithdrawals> updateAllBalances() {
    List<TimeDeposit> deposits = timeDepositStore.findAllTimeDeposits();

    timeDepositCalculator.updateBalance(deposits);
    timeDepositStore.saveBalances(deposits);

    return timeDepositStore.findAll();
  }
}
