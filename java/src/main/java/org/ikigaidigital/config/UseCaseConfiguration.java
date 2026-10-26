package org.ikigaidigital.config;

import org.ikigaidigital.application.port.in.UpdateTimeDepositBalanceUseCase;
import org.ikigaidigital.application.service.TimeDepositCalculator;
import org.ikigaidigital.application.service.interest.InterestRateStrategyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

  @Bean
  UpdateTimeDepositBalanceUseCase updateTimeDepositBalanceUseCase(
      InterestRateStrategyFactory interestRateStrategyFactory) {
    return new TimeDepositCalculator(interestRateStrategyFactory);
  }
}
