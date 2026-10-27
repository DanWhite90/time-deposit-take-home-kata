package org.ikigaidigital.adapter.in.web;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.IntegrationTest;
import org.ikigaidigital.TimeDepositTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@DisplayName("TimeDepositController")
@RequiredArgsConstructor
class TimeDepositControllerTest {

  private static final String API_DOCS = "/v3/api-docs";

  private static final String LIST_OPERATION = "$.paths['/time-deposits'].get";

  private static final String UPDATE_OPERATION = "$.paths['/time-deposits/balances'].post";

  private static final String OK_ITEMS_REF = ".responses['200'].content['application/json'].schema.items['$ref']";

  private static final String TIME_DEPOSIT_SCHEMA = "$.components.schemas.TimeDeposit";

  private static final String WITHDRAWAL_SCHEMA = "$.components.schemas.Withdrawal";

  private static final String TIME_DEPOSIT_REF = "#/components/schemas/TimeDeposit";

  private static final String WITHDRAWAL_REF = "#/components/schemas/Withdrawal";

  private final MockMvc mockMvc;

  private final TimeDepositTestData testData;

  @BeforeEach
  void seedStore() {
    testData.clear();
    testData.insertTimeDeposit(1, "basic", 45, "1234567.00");
    testData.insertWithdrawal(9, 1, "25.50", LocalDate.of(2026, 1, 15));
    testData.insertTimeDeposit(2, "basic", 30, "500.00");
  }

  @Nested
  @DisplayName("listTimeDeposits()")
  class ListTimeDeposits {

    @Test
    @DisplayName("returns every plan with its withdrawals")
    void listTimeDepositsReturnsEveryPlanWithItsWithdrawals() throws Exception {
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
    @DisplayName("applies one month of interest and returns the updated plans")
    void updateTimeDepositBalancesAppliesInterestAndReturnsTheUpdatedPlans() throws Exception {
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

  @Nested
  @DisplayName("OpenAPI document")
  class OpenApiDocument {

    @Test
    @DisplayName("describes both endpoints with the TimeDeposit and Withdrawal schemas")
    void openApiDocumentDescribesBothEndpointsAndSchemas() throws Exception {
      mockMvc.perform(get(API_DOCS))
          .andExpect(status().isOk())
          .andExpect(jsonPath(LIST_OPERATION + ".operationId").value("listTimeDeposits"))
          .andExpect(jsonPath(LIST_OPERATION + OK_ITEMS_REF).value(TIME_DEPOSIT_REF))
          .andExpect(jsonPath(UPDATE_OPERATION + ".operationId").value("updateTimeDepositBalances"))
          .andExpect(jsonPath(UPDATE_OPERATION + OK_ITEMS_REF).value(TIME_DEPOSIT_REF))
          .andExpect(jsonPath(TIME_DEPOSIT_SCHEMA + ".required").value(
              containsInAnyOrder("id", "planType", "balance", "days", "withdrawals")))
          .andExpect(jsonPath(TIME_DEPOSIT_SCHEMA + ".properties.balance.type").value("number"))
          .andExpect(jsonPath(TIME_DEPOSIT_SCHEMA + ".properties.withdrawals.items['$ref']").value(WITHDRAWAL_REF))
          .andExpect(jsonPath(WITHDRAWAL_SCHEMA + ".properties.amount.type").value("number"))
          .andExpect(jsonPath(WITHDRAWAL_SCHEMA + ".properties.date.format").value("date"));
    }
  }
}
