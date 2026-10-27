package org.ikigaidigital;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.simple.JdbcClient;

/**
 * Seeds the tables with plain SQL, so production code needs no test-only repositories or entity constructors.
 */
@TestComponent
@RequiredArgsConstructor
public class TimeDepositTestData {

  private static final String DELETE_WITHDRAWALS = "DELETE FROM withdrawals";

  private static final String DELETE_TIME_DEPOSITS = "DELETE FROM \"timeDeposits\"";

  private static final String INSERT_TIME_DEPOSIT =
      "INSERT INTO \"timeDeposits\" (id, \"planType\", days, balance) VALUES (?, ?, ?, ?)";

  private static final String INSERT_WITHDRAWAL =
      "INSERT INTO withdrawals (id, \"timeDepositId\", amount, date) VALUES (?, ?, ?, ?)";

  private final JdbcClient jdbcClient;

  public void clear() {
    jdbcClient.sql(DELETE_WITHDRAWALS).update();
    jdbcClient.sql(DELETE_TIME_DEPOSITS).update();
  }

  public void insertTimeDeposit(final int id, final String planType, final int days, final String balance) {
    jdbcClient.sql(INSERT_TIME_DEPOSIT).params(id, planType, days, new BigDecimal(balance)).update();
  }

  public void insertWithdrawal(final int id, final int timeDepositId, final String amount, final LocalDate date) {
    jdbcClient.sql(INSERT_WITHDRAWAL).params(id, timeDepositId, new BigDecimal(amount), date).update();
  }
}
