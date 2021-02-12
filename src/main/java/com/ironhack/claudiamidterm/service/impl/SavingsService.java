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
import java.util.*;

@Service
public class SavingsService<SavingsRepository> implements ISavingsService {

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    public SavingsAccount create(SavingsAccountDTO savingsAccountDTO) {
        Optional<AccountHolder> accountHolder1=accountHolderRepository.findById(savingsAccountDTO.getPrimaryOwnerId());

        SavingsAccount savings = new SavingsAccount(accountHolder1.get(),
                new Money(savingsAccountDTO.getBalance()),
                savingsAccountDTO.getSecretKey());

        if (savingsAccountDTO.getSecondaryOwnerId() != null) {
            Optional<AccountHolder> accountHolder2=accountHolderRepository.findById(savingsAccountDTO.getSecondaryOwnerId());
            savings.setSecondaryOwner(accountHolder2.get());

        }

        if (savingsAccountDTO.getInterestRate() != null) {
            savings.setInterestRate(savingsAccountDTO.getInterestRate());
        }

        if (savingsAccountDTO.getMinimumBalance() != null) {
            savings.setMinimumBalance(new Money(savingsAccountDTO.getMinimumBalance()));
        }



        return savingsAccountRepository.save(savings);
    }
}
