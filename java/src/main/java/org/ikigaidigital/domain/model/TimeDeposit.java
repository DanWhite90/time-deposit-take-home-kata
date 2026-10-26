package org.ikigaidigital.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/* Cleanup using lombok instead of records only to not introduce breaking changes, as per requirement of the
assignment under the assumption the class is to be considered a shared model across repos and services. Ideally I would
have made it immutable with toBuilder() logic. */
@Getter
@AllArgsConstructor
public class TimeDeposit {

  private int id;

  private String planType;

  @Setter
  private Double balance;

  private int days;

}
