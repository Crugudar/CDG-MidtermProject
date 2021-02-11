package com.ironhack.claudiamidterm.repository;


import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Repository
public interface TransferenceRepository extends JpaRepository<Transference, Long>{

    //SELECT transaction_date_time, SUM(transaction_amount) AS sum FROM transaction WHERE origin_id = :originId AND transaction_date_time >= NOW() - INTERVAL 1 DAY
    //SELECT MAX(t.sum) FROM (SELECT DATE(transaction_date_time) AS transaction_date, SUM(transaction_amount) AS sum FROM transaction WHERE origin_id = :originId GROUP BY transaction_date) AS t

    @Query(value = "SELECT SUM(amount) FROM transference WHERE origin_account = :id AND transference_date >= NOW() - INTERVAL 1 DAY", nativeQuery = true)
    public BigDecimal sumLastDayTransferences(@Param("id")Long id);

    @Query(value = "SELECT * FROM transference WHERE origin_account = :id AND transference_date >= NOW() - INTERVAL 1 SECOND", nativeQuery = true)
    public List<Transference> lastSecondTransferences(@Param("id")Long id);

    @Query(value = "SELECT SUM(amount), DAY(transference_date) FROM transference WHERE origin_account = :id GROUP BY DAY(transference_date)",nativeQuery = true)
    public List<BigDecimal> sumOfTransferenceByDay(@Param("id") Long id);
}
