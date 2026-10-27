package org.ikigaidigital.domain.interest;

import java.math.BigDecimal;
import org.ikigaidigital.TimeDeposit;

public interface InterestRateStrategy {

  /**
   * Returns one month of interest, rounded half up to cents. Implementations multiply by the annual rate first, which
   * is exact, and divide by twelve last, so rounding to cents is the only rounding.
   */
  BigDecimal interest(TimeDeposit deposit);
}
