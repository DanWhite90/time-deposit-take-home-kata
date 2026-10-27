package org.ikigaidigital.application.port.out;

import java.util.List;
import org.ikigaidigital.TimeDeposit;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;

public interface TimeDepositStore {

  List<TimeDepositWithWithdrawals> findAll();

  // Without withdrawals: the interest calculation ignores them, so they are not loaded.
  List<TimeDeposit> findAllTimeDeposits();

  void saveBalances(List<TimeDeposit> deposits);
}
