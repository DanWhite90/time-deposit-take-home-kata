package org.ikigaidigital.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoneyUtil {

  /* Keep new BigDecimal(double) as in the original calculator. BigDecimal.valueOf(double) rounds some values
  differently, e.g. 1.005 becomes 1.01 instead of 1.00, which would change existing balances. */
  public static double round(final double interest) {
    return new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

}
