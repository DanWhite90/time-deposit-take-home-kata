package org.ikigaidigital.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoneyUtil {

  public static double round(final double interest) {
    return new BigDecimal(interest).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

}
