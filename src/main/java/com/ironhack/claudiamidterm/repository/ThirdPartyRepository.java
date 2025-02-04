package com.ironhack.claudiamidterm.repository;

import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
    ThirdParty findByName(String name);
    ThirdParty findByHashKey(String key);
}
