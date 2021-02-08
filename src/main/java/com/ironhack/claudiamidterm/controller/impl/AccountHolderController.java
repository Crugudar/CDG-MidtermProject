package com.ironhack.claudiamidterm.controller.impl;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.controller.interfaces.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountHolderController implements IAccountHolderController{
    @Autowired
    IAccountHolderService accountHolderService;

    @PostMapping("/new/accountHolder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder create(@RequestBody AccountHolderDTO accountHolderDTO) {
      return accountHolderService.create(accountHolderDTO);
    }

}
