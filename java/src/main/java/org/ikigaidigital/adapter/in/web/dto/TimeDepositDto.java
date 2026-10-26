package org.ikigaidigital.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.ikigaidigital.domain.model.TimeDepositWithWithdrawals;

@Schema(name = "TimeDeposit", requiredProperties = {"id", "planType", "balance", "days", "withdrawals"})
public record TimeDepositDto(
    @Schema(example = "1") int id,
    @Schema(example = "basic") String planType,
    @Schema(example = "1000.0") Double balance,
    @Schema(example = "45") int days,
    List<WithdrawalDto> withdrawals) {

  public static TimeDepositDto from(final TimeDepositWithWithdrawals deposit) {
    return new TimeDepositDto(
        deposit.id(),
        deposit.planType(),
        deposit.balance(),
        deposit.days(),
        deposit.withdrawals().stream().map(WithdrawalDto::from).toList());
  }
}
