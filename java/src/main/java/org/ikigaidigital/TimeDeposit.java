package org.ikigaidigital;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/* Given the refactoring constraints at point 4 of the assignment, moving or changing this class would be a breaking
change. I kept it here with same package and cleaned the definition using the equivalent Lombok annotations,
generating the exact same logic. Ideally this could have been a record (immutable). The balance should have been a
BigDecimal, to compensate all other operations on money are made using BigDecimal.
 */
@Getter
@AllArgsConstructor
public class TimeDeposit {

  private int id;

  private String planType;

  @Setter
  private Double balance;

  private int days;

}
