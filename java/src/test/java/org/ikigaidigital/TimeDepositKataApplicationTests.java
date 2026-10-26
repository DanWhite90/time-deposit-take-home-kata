package org.ikigaidigital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PostgresTestcontainersConfiguration.class)
@DisplayName("TimeDepositKataApplication")
class TimeDepositKataApplicationTests {

  @Nested
  @DisplayName("application context")
  class ApplicationContext {

    @Test
    @DisplayName("loads")
    void contextLoads() {
    }
  }
}
