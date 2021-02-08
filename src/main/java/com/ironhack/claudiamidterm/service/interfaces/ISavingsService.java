package com.ironhack.claudiamidterm.service.interfaces;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;

public interface ISavingsService {
    SavingsAccount create(SavingsAccountDTO savingsAccountDTO);
}
