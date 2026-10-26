package org.ikigaidigital.application.port.in;

import java.util.List;
import org.ikigaidigital.domain.model.TimeDeposit;

public interface UpdateTimeDepositBalanceUseCase {

  void updateBalance(List<TimeDeposit> xs);
}
