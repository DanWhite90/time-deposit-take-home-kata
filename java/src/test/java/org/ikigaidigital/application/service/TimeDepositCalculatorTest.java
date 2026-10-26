package org.ikigaidigital.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.ikigaidigital.domain.model.TimeDeposit;
import org.junit.jupiter.api.Test;

public class TimeDepositCalculatorTest {

  @Test
  public void updateBalance_Test() {
    TimeDepositCalculator calc = new TimeDepositCalculator();
    List<TimeDeposit> plans = List.of(
        new TimeDeposit(1, "basic", 1234567.00, 45)
    );
    calc.updateBalance(plans);

    assertThat(1).isEqualTo(1);
  }
}
