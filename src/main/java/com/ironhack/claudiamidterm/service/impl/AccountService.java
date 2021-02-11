package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.*;
import java.util.*;
import java.util.logging.*;

@Service
public class AccountService implements IAccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransferenceRepository transferenceRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FraudChecker fraudChecker;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Account updateBalance(long id, String balance, CredentialsDTO credentials) {
        Optional<User> user=userRepository.findByUsername(credentials.getUsername());

        Account updated=accountRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Account not found"));

        boolean isAdmin = user.get().getRoles().stream().anyMatch(x ->x.getRole().equals(UserRole.ADMIN));

            Money newBalance=null;
            try{
                newBalance=new Money(new BigDecimal(balance));
                System.out.println("new balance "+newBalance.getAmount());
            }catch (Exception e){
                System.out.println("new balance indicated is not a number");
            }


            try{
                Boolean matches=passwordEncoder.matches(credentials.getPassword(), user.get().getPassword());
                System.out.println("password matches "+matches.toString());
            }catch (Exception e){
                System.out.println("new balance indicated is not a number");
            }


            if(newBalance!=null){
                    updated.setBalance(newBalance);

            }
            return updated;

    }
    }
