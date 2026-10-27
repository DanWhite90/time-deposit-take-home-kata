package org.ikigaidigital.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.PostgresTestcontainersConfiguration;
import org.ikigaidigital.adapter.out.persistence.TimeDepositEntity;
import org.ikigaidigital.adapter.out.persistence.TimeDepositJpaRepository;
import org.ikigaidigital.adapter.out.persistence.WithdrawalEntity;
import org.ikigaidigital.adapter.out.persistence.WithdrawalJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestcontainersConfiguration.class)
@DisplayName("TimeDepositController")
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
class TimeDepositControllerTest {

  private final MockMvc mockMvc;

  private final TimeDepositJpaRepository timeDeposits;

  private final WithdrawalJpaRepository withdrawals;

  @BeforeEach
  void seedStore() {
    withdrawals.deleteAll();
    timeDeposits.deleteAll();

    TimeDepositEntity basic = timeDeposits.save(
        new TimeDepositEntity(1, "basic", 45, new BigDecimal("1234567.00")));

    withdrawals.save(new WithdrawalEntity(9, basic, new BigDecimal("25.50"), LocalDate.of(2026, 1, 15)));
    timeDeposits.save(new TimeDepositEntity(2, "basic", 30, new BigDecimal("500.00")));
  }

  @Nested
  @DisplayName("listTimeDeposits()")
  class ListTimeDeposits {

    @Test
    @DisplayName("list time deposits response matches OpenAPI schema")
    void listTimeDepositsMatchesTheOpenApiSchema() throws Exception {
      mockMvc.perform(get("/time-deposits"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].planType").value("basic"))
          .andExpect(jsonPath("$[0].balance").value(1234567.00))
          .andExpect(jsonPath("$[0].days").value(45))
          .andExpect(jsonPath("$[0].withdrawals[0].id").value(9))
          .andExpect(jsonPath("$[0].withdrawals[0].timeDepositId").value(1))
          .andExpect(jsonPath("$[0].withdrawals[0].amount").value(25.5))
          .andExpect(jsonPath("$[0].withdrawals[0].date").value("2026-01-15"))
          .andExpect(jsonPath("$[1].id").value(2))
          .andExpect(jsonPath("$[1].withdrawals").isEmpty());
    }
  }

  @Nested
  @DisplayName("updateTimeDepositBalances()")
  class UpdateTimeDepositBalances {

    @Test
    @DisplayName("updating balances response matches OpenAPI schema")
    void updateTimeDepositBalancesAppliesInterestAndReturnsTheSameSchema() throws Exception {
      mockMvc.perform(post("/time-deposits/balances"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(1))
          .andExpect(jsonPath("$[0].planType").value("basic"))
          .andExpect(jsonPath("$[0].balance").value(1235595.81))
          .andExpect(jsonPath("$[0].days").value(45))
          .andExpect(jsonPath("$[0].withdrawals[0].id").value(9))
          .andExpect(jsonPath("$[0].withdrawals[0].date").value("2026-01-15"))
          .andExpect(jsonPath("$[1].id").value(2))
          .andExpect(jsonPath("$[1].balance").value(500.00))
          .andExpect(jsonPath("$[1].days").value(30));
    }
  }
}
