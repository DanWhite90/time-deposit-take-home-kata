package org.ikigaidigital.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoneyUtil {

  private static final int CENTS = 2;

  public static BigDecimal round(final BigDecimal amount) {
    return amount.setScale(CENTS, RoundingMode.HALF_UP);
  }

}
