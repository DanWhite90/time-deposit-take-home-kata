package org.ikigaidigital.application.service.interest;

import java.math.BigDecimal;
import org.ikigaidigital.TimeDeposit;

public interface InterestRateStrategy {

  /**
   * Returns one month of unrounded interest. The monthly rate is a truncated non-terminating decimal (e.g. 0.01 / 12),
   * so implementations multiply with {@code DECIMAL64}: an exact half-cent like 115.405 stays 115.405 instead of
   * 115.40499..., and rounding half up to cents gives 115.41.
   */
  BigDecimal interest(TimeDeposit deposit);
}
