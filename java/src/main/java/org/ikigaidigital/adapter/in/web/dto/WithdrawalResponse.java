package org.ikigaidigital.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.ikigaidigital.domain.model.Withdrawal;

@Schema(name = "Withdrawal")
public record WithdrawalResponse(
    @Schema(example = "9") int id,
    @Schema(example = "1") int timeDepositId,
    @Schema(example = "25.5") Double amount,
    @Schema(format = "date", example = "2026-01-15") LocalDate date) {

  static WithdrawalResponse from(Withdrawal withdrawal) {
    return new WithdrawalResponse(
        withdrawal.id(),
        withdrawal.timeDepositId(),
        withdrawal.amount(),
        withdrawal.date());
  }
}
