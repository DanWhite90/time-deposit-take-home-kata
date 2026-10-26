package org.ikigaidigital.infrastructure;

import org.ikigaidigital.application.port.in.UpdateTimeDepositBalanceUseCase;
import org.ikigaidigital.application.service.TimeDepositCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

  @Bean
  UpdateTimeDepositBalanceUseCase updateTimeDepositBalanceUseCase() {
    return new TimeDepositCalculator();
  }
}
