package com.ironhack.claudiamidterm.controller.impl;


import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.core.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;

@RestController
public class AccountController {

    @Autowired
    IAccountService accountService;

    @PatchMapping("/admin/update/{id}/{newBalance}")
    @ResponseStatus(HttpStatus.OK)
    public Account update(@PathVariable ("id")String id, @PathVariable ("newBalance") String newBalance, @RequestBody CredentialsDTO credentials) {

        return accountService.updateBalance(Long.parseLong(id),newBalance, credentials);
    }

//    @PostMapping("/accountHolder/transference")
//    @ResponseStatus(HttpStatus.OK)
//    public Transference transferFunds(@RequestBody TransferenceDTO transferenceDTO){
//        return accountService.transfer(transferenceDTO);
//    }
}
