package com.ironhack.claudiamidterm.repository;

import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByPrimaryOwnerIdOrSecondaryOwnerId(Long id, Long secId);
}
