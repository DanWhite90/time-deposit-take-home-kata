package org.ikigaidigital;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/* Shared assignment model. It keeps its original package, constructor and accessors so existing callers compile
unchanged. Lombok generates the same public API as the original hand-written class. I used Lombok instead of a record
only to avoid breaking changes: I assume the class is a shared model across repos and services. Ideally I would have
made it immutable with toBuilder() logic. */
@Getter
@AllArgsConstructor
public class TimeDeposit {

  private int id;

  private String planType;

  @Setter
  private Double balance;

  private int days;

}
