package org.ikigaidigital.adapter.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimeDepositJpaRepository extends JpaRepository<TimeDepositEntity, Integer> {

  @Query("""
      select distinct deposit
      from TimeDepositEntity deposit
      left join fetch deposit.withdrawals
      order by deposit.id
      """)
  List<TimeDepositEntity> findAllWithWithdrawals();

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("update TimeDepositEntity deposit set deposit.balance = :balance where deposit.id = :id")
  void updateBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);
}
