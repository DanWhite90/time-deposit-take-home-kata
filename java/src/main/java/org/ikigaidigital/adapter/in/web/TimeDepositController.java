package org.ikigaidigital.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ikigaidigital.adapter.in.web.dto.TimeDepositDto;
import org.ikigaidigital.application.port.in.ListTimeDepositsUseCase;
import org.ikigaidigital.application.port.in.UpdateAllTimeDepositBalancesUseCase;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inbound adapter for listing time deposits and updating their balances.
 */
@RestController
@RequestMapping(path = "/time-deposits", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "time-deposits", description = "Retrieve time deposits and update their balances")
@RequiredArgsConstructor
public class TimeDepositController {

  private final ListTimeDepositsUseCase listTimeDepositsUseCase;

  private final UpdateAllTimeDepositBalancesUseCase updateAllTimeDepositBalancesUseCase;

  @GetMapping
  @Operation(operationId = "listTimeDeposits", summary = "Retrieve all time deposits")
  @ApiResponse(
      responseCode = "200",
      description = "Every time deposit, including its withdrawals",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeDepositDto.class))))
  public List<TimeDepositDto> listTimeDeposits() {
    return listTimeDepositsUseCase.listTimeDeposits().stream()
        .map(TimeDepositDto::from)
        .toList();
  }

  @PostMapping("/balances")
  @Operation(
      operationId = "updateTimeDepositBalances",
      summary = "Update the balance of every time deposit",
      description = "Loads every stored time deposit, applies the existing monthly interest rules, "
          + "stores the new balances, and returns the plans. POST is used because the client does not "
          + "send a replacement resource.")
  @ApiResponse(
      responseCode = "200",
      description = "Time deposits after balances were updated",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = TimeDepositDto.class))))
  public List<TimeDepositDto> updateTimeDepositBalances() {
    return updateAllTimeDepositBalancesUseCase.updateAllBalances().stream()
        .map(TimeDepositDto::from)
        .toList();
  }
}
