package com.ironhack.claudiamidterm.repository;

import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
    public AccountHolder findByUsername(String username);
    public AccountHolder findByName(String name);

}
