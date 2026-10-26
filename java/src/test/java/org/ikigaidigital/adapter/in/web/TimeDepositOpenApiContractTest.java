package org.ikigaidigital.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TimeDepositOpenApiContractTest {

  @Test
  void contractDeclaresOnlyTheTwoExerciseEndpoints() throws Exception {
    String contract = Files.readString(Path.of("src/main/resources/static/openapi/time-deposit-api.yaml"));

    assertThat(contract.lines().filter(line -> line.startsWith("  /")).toList())
        .containsExactly("  /time-deposits:", "  /time-deposits/balances:");
    assertThat(contract).contains("operationId: listTimeDeposits");
    assertThat(contract).contains("operationId: updateTimeDepositBalances");
    assertThat(contract).contains("planType");
    assertThat(contract).contains("withdrawals");
    assertThat(contract).contains("timeDepositId");
  }
}
