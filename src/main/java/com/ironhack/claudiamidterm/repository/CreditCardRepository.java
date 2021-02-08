package com.ironhack.claudiamidterm.repository;

import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
