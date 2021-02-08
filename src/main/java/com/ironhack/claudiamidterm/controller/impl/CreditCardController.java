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
public class CreditCardController implements ICreditCardController {

    @Autowired
    ICreditCardService creditCardService;

    @PostMapping("/new/creditCard")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard create(@RequestBody @Valid CreditCardDTO creditCardDTO) {
        return creditCardService.create(creditCardDTO);
    }
}
