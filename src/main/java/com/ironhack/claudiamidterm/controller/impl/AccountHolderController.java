package com.ironhack.claudiamidterm.controller.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.controller.interfaces.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.*;
import java.security.*;
import java.util.*;

@RestController
public class AccountHolderController implements IAccountHolderController{
    @Autowired
    IAccountHolderService accountHolderService;

    @PostMapping("/new/accountHolder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder create(@RequestBody AccountHolderDTO accountHolderDTO) {
      return accountHolderService.create(accountHolderDTO);
    }

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAllAccounts(@RequestBody CredentialsDTO credentials, Principal principal){
        return accountHolderService.getAllAccounts(credentials, principal.getName());
    }

    @GetMapping("/accounts/{id}/check-balance")
    @ResponseStatus(HttpStatus.OK)
    public Money getAccountBalance(@PathVariable String id, @RequestBody CredentialsDTO credentials, Principal principal){
        return  accountHolderService.getAccountBalance(Long.parseLong(id), credentials, principal.getName());
    }

    @PostMapping("/accounts/newTransfer")
    @ResponseStatus(HttpStatus.CREATED)
    public Transference create(@RequestBody TransferenceDTO transferenceDTO, Principal principal) {
        return accountHolderService.transfer(transferenceDTO, principal);
    }


}
