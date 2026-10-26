package org.ikigaidigital.adapter.out.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ikigaidigital.application.port.out.TimeDepositStore;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;
import org.springframework.stereotype.Component;

/**
 * Holds plans in memory so the two endpoints can run before a database adapter exists.
 */
@Component
public class InMemoryTimeDepositStore implements TimeDepositStore {

  private final List<TimeDepositWithWithdrawals> deposits = new ArrayList<>();

  @Override
  public synchronized List<TimeDepositWithWithdrawals> findAll() {
    return List.copyOf(deposits);
  }

  @Override
  public synchronized void saveBalances(List<TimeDeposit> updated) {
    Map<Integer, Double> balancesById = new HashMap<>();
    for (TimeDeposit deposit : updated) {
      balancesById.put(deposit.getId(), deposit.getBalance());
    }
    for (int i = 0; i < deposits.size(); i++) {
      TimeDepositWithWithdrawals current = deposits.get(i);
      Double balance = balancesById.get(current.id());
      if (balance != null) {
        deposits.set(i, current.withBalance(balance));
      }
    }
  }

  public synchronized void add(TimeDepositWithWithdrawals deposit) {
    deposits.add(deposit);
  }

  public synchronized void clear() {
    deposits.clear();
  }
}
