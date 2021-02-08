package com.ironhack.claudiamidterm.service.interfaces;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;

public interface ICheckingAccountService {
    public StudentChecking create(CheckingAccountDTO checkingAccountDTO);
}
