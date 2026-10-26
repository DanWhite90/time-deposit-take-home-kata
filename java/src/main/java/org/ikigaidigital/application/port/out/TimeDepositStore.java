package org.ikigaidigital.application.port.out;

import java.util.List;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;

public interface TimeDepositStore {

  List<TimeDepositWithWithdrawals> findAll();

  void saveBalances(List<TimeDeposit> deposits);
}
