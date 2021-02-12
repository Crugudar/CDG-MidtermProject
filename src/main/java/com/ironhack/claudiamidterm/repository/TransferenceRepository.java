package com.ironhack.claudiamidterm.repository;


import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Repository
public interface TransferenceRepository extends JpaRepository<Transference, Long>{


    //Sacar dinero transferido en las ultimas 24h
    @Query(value = "SELECT SUM(amount) AS sum FROM transference WHERE origin_account = :id AND transference_date >= NOW() - INTERVAL 1 DAY", nativeQuery = true)
    public BigDecimal sumLastDayTransferences(@Param("id")Long id);

    //Máximo de dinero transferido en 24h
    @Query(value = "SELECT MAX(t.sum) FROM (SELECT DATE(transference_date), SUM(amount) AS sum FROM transference WHERE origin_account = :id GROUP BY transference_date) AS t",nativeQuery = true)
    public BigDecimal maxOfTransferenceinOneDay(@Param("id") Long id);

    //3601
    @Query(value = "SELECT * FROM transference WHERE origin_account = :id AND transference_date >= NOW() - INTERVAL 3601 SECOND", nativeQuery = true)
    public List<Transference> lastSecondTransferences(@Param("id")Long id);


}
