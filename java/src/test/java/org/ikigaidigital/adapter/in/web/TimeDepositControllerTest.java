package org.ikigaidigital.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.ikigaidigital.adapter.out.persistence.InMemoryTimeDepositStore;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;
import org.ikigaidigital.domain.model.Withdrawal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TimeDepositController")
class TimeDepositControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private InMemoryTimeDepositStore timeDepositStore;

  @BeforeEach
  void seedStore() {
    timeDepositStore.clear();
    timeDepositStore.add(new TimeDepositWithWithdrawals(
        1,
        "basic",
        1234567.00,
        45,
        List.of(new Withdrawal(9, 1, 25.5, LocalDate.of(2026, 1, 15)))));
    timeDepositStore.add(new TimeDepositWithWithdrawals(2, "basic", 500.00, 30, List.of()));
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
