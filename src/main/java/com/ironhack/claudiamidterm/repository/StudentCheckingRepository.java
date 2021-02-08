package com.ironhack.claudiamidterm.repository;

import com.ironhack.claudiamidterm.model.*;
import org.springframework.data.jpa.repository.*;

public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Long> {
}
