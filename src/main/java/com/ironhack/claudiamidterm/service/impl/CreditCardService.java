package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Override
    public CreditCard create(CreditCardDTO creditCardDTO) {

        Optional<AccountHolder> accountHolder1=accountHolderRepository.findById(creditCardDTO.getPrimaryOwnerId());


        CreditCard creditCard = new CreditCard(accountHolder1.get(), new Money(creditCardDTO.getBalance()));

        if (creditCardDTO.getCreditLimit()!=null){
            creditCard.setCreditLimit(new Money(creditCardDTO.getCreditLimit()));

        }
        if (creditCardDTO.getInterestRate()!=null){
            creditCard.setInterestRate(creditCardDTO.getInterestRate());
        }
        if (creditCardDTO.getSecondaryOwnerId()!=null) {
            Optional<AccountHolder> accountHolder2=accountHolderRepository.findById(creditCardDTO.getSecondaryOwnerId());
            creditCard.setSecondaryOwner(accountHolder2.get());

        }


        System.out.println("New CreditCard Account created!!");
        return creditCardRepository.save(creditCard);
    }
}
