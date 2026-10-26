package org.ikigaidigital.application.port.in;

import java.util.List;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;

public interface ListTimeDepositsUseCase {

  List<TimeDepositWithWithdrawals> listTimeDeposits();
}
