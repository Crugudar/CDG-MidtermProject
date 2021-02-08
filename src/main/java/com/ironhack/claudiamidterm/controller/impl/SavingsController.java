package com.ironhack.claudiamidterm.controller.impl;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.controller.interfaces.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;

@RestController
public class SavingsController implements ISavingsController {

    @Autowired
    ISavingsService savingsService;

    @PostMapping("/new/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount create(@RequestBody @Valid SavingsAccountDTO savingsAccountDTO) {
        return savingsService.create(savingsAccountDTO);
    }
}
