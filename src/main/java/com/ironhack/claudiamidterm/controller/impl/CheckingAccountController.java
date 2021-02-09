package com.ironhack.claudiamidterm.controller.impl;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.controller.interfaces.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.security.*;

@RestController
public class CheckingAccountController implements ICheckingAccountController {

    @Autowired
    ICheckingAccountService checkingAccountService;

    @PostMapping("/new/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentChecking create(@RequestBody @Valid CheckingAccountDTO checkingAccountDTO) {
        return checkingAccountService.create(checkingAccountDTO);
    }

}
