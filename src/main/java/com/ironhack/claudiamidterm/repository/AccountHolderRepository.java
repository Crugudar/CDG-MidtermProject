package com.ironhack.claudiamidterm.repository;

import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
}
