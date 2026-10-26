package org.ikigaidigital.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;

@Schema(name = "TimeDeposit", requiredProperties = {"id", "planType", "balance", "days", "withdrawals"})
public record TimeDepositResponse(
    @Schema(example = "1") int id,
    @Schema(example = "basic") String planType,
    @Schema(example = "1000.0") Double balance,
    @Schema(example = "45") int days,
    List<WithdrawalResponse> withdrawals) {

  public static TimeDepositResponse from(TimeDepositWithWithdrawals deposit) {
    return new TimeDepositResponse(
        deposit.id(),
        deposit.planType(),
        deposit.balance(),
        deposit.days(),
        deposit.withdrawals().stream().map(WithdrawalResponse::from).toList());
  }
}
