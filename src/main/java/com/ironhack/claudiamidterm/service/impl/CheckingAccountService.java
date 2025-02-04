package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

import static com.ironhack.claudiamidterm.utils.Calculos.calculateYears;

@Service
public class CheckingAccountService implements ICheckingAccountService {

    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public StudentChecking create(CheckingAccountDTO checkingAccountDTO) {

        Optional<AccountHolder> accountHolder1=accountHolderRepository.findById(checkingAccountDTO.getPrimaryOwnerId());

        if (calculateYears(accountHolder1.get().getDateOfBirth()) < 24) {
            System.out.println("A Student Account will be created since the Account Holder is under 24");

                StudentChecking studentChecking = new StudentChecking(accountHolder1.get(),new Money(checkingAccountDTO.getBalance()),
                checkingAccountDTO.getSecretKey(), AccountStatus.ACTIVE);

            if (checkingAccountDTO.getSecondaryOwnerId()!=null) {
                Optional<AccountHolder> accountHolder2 =accountHolderRepository.findById(checkingAccountDTO.getSecondaryOwnerId());
                studentChecking.setSecondaryOwner(accountHolder2.get());
            }
            System.out.println("New Student Checking Account created");
            return studentCheckingRepository.save(studentChecking);

        } else {
                CheckingAccount checking = new CheckingAccount(accountHolder1.get(),new Money(checkingAccountDTO.getBalance()),
                checkingAccountDTO.getSecretKey(),AccountStatus.ACTIVE);
            if (checkingAccountDTO.getSecondaryOwnerId()!=null) {
                Optional<AccountHolder> accountHolder2 =accountHolderRepository.findById(checkingAccountDTO.getSecondaryOwnerId());
                checking.setSecondaryOwner(accountHolder2.get());
            }
            System.out.println("New Checking Account created");
            return checkingAccountRepository.save(checking);
        }
    }


}
