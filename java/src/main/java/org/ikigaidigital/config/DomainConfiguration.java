package org.ikigaidigital.config;

import org.ikigaidigital.TimeDepositCalculator;
import org.ikigaidigital.domain.interest.InterestRateStrategyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the framework-free domain classes as Spring beans.
 */
@Configuration
public class DomainConfiguration {

  @Bean
  public InterestRateStrategyFactory interestRateStrategyFactory() {
    return new InterestRateStrategyFactory();
  }

  @Bean
  public TimeDepositCalculator timeDepositCalculator(final InterestRateStrategyFactory interestRateStrategyFactory) {
    return new TimeDepositCalculator(interestRateStrategyFactory);
  }
}
