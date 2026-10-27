package org.ikigaidigital.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Withdrawal(int id, int timeDepositId, BigDecimal amount, LocalDate date) {
}
