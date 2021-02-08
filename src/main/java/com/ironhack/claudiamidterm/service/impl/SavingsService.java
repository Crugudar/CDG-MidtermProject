package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.time.*;

@Service
public class SavingsService<SavingsRepository> implements ISavingsService {

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    public SavingsAccount create(SavingsAccountDTO savingsAccountDTO) {
        AccountHolder accountHolder1 = accountHolderRepository.findById(savingsAccountDTO.getPrimaryOwnerId()).orElseThrow(()-> new IllegalArgumentException("User not found"));
        AccountHolder accountHolder2 = savingsAccountDTO.getSecondaryOwnerId() != null ? accountHolderRepository.findById(savingsAccountDTO.getSecondaryOwnerId()).orElseThrow(()-> new IllegalArgumentException("User not found")) : null ;

        SavingsAccount savings = new SavingsAccount(accountHolder1,
                new Money(savingsAccountDTO.getBalance()),
                savingsAccountDTO.getSecretKey(),
                savingsAccountDTO.getStatus());

        if (accountHolder2 != null) {savings.setSecondaryOwner(accountHolder2);}

        savings.setLastInterestUpdate(LocalDate.now());


        return savingsAccountRepository.save(savings);
    }
}
