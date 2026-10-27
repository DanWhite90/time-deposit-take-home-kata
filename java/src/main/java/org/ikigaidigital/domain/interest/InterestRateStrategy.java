package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import org.ikigaidigital.TimeDeposit;

public interface InterestRateStrategy {

  /**
   * Returns one month of interest, rounded half up to cents. Implementations apply the rate in {@code double} and
   * round with {@code new BigDecimal(double)}, exactly as the original calculator did, because the assignment requires
   * {@code updateBalance} to keep its behavior. That includes the original's half-cent rounding flaw; see the README.
   */
  BigDecimal interest(TimeDeposit deposit);
}
