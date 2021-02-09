package com.ironhack.claudiamidterm.repository;


import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Repository
public interface TransferenceRepository extends JpaRepository<Transference, Long>{

    @Query(value = "SELECT SUM(amount) FROM transference WHERE origin_account = :id AND transference_date >= NOW() - INTERVAL 1 DAY", nativeQuery = true)
    public BigDecimal sumLastDayTransferences(Long id);

    /**
     * Returns a List of the transferences done by a given id account in the last second
     * Has 3601 seconds because an adjustment on the UTC configuration
     * **/
    @Query(value = "SELECT * FROM transference WHERE origin_account = :id AND   transference_date >= NOW() - INTERVAL 3601 SECOND", nativeQuery = true)
    public List<Transference> lastSecondTransferences(Long id);

    /**
     * Returns a List of sums of the transferences amounts done by a given id account in
     * **/
    @Query(value = "SELECT SUM(amount), DAY(transference_date) FROM transference WHERE origin_account = :id GROUP BY DAY(transference_date)",nativeQuery = true)
    public List<BigDecimal> sumOfTransferenceByDay(Long id);
}
