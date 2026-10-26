package org.ikigaidigital.domain.model;

import java.time.LocalDate;

public record Withdrawal(int id, int timeDepositId, Double amount, LocalDate date) {
}
