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
        try{

            boolean isAdmin = user.get().getRoles().stream().anyMatch(x ->x.getRole().equals(UserRole.ADMIN));
            Money newBalance=new Money(new BigDecimal(balance));
            System.out.println("new balance "+newBalance.getAmount());
            Boolean matches=passwordEncoder.matches(credentials.getPassword(), user.get().getPassword());

            System.out.println("password matches "+matches.toString());


            Optional<Account> updated=accountRepository.findById(id);

            System.out.println("updated "+updated.toString());
            updated.get().setBalance(newBalance);
            return updated.get();
        }catch (Exception e){
            throw new IllegalArgumentException("credentials do not correspond with any user or the new balance you provided is not a number");
        }
    }
//
//
//    @Override
//    public Transference transfer(TransferenceDTO transferenceDto) {
//        Optional<AccountHolder> originAccount = accountHolderRepository.findById(transferenceDto.getOriginId());
//        Optional<User> user = userRepository.findByUsername(originAccount.get().getUsername());
//        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(user.get().getId());
//
//        if(!accountRepository.findAllByPrimaryOwnerIdOrSecondaryOwnerId(accountHolder.get().getId(),accountHolder.get().getId()).contains(originAccount)){
//            throw new IllegalArgumentException("The Account does not belong to the logged user");
//        }
//        if(!fraudChecker.firstCondition(transferenceDto) || !fraudChecker.secondCondition(transferenceDto)) {
//            LOGGER.error("The account has met some fraud detection conditions");
//            throw new IllegalArgumentException("This transaction can not be done due to fraud detections"); }
//        if(transferenceDto.getAmount().compareTo(originAccount.get().getBalance().getAmount()) > 0 ){
//            LOGGER.error("The account has not enough funds");
//            throw new IllegalArgumentException("There are not enough funds in the account");
//        }
//
//        Optional<Account> destinationAccount = accountRepository.findById(transferenceDto.getDestinationId());
//        Money amount = new Money(transferenceDto.getAmount());
//
//        originAccount.get().getBalance().decreaseAmount(amount);
//        destinationAccount.get().getBalance().increaseAmount(amount);
//        Transference transference = new Transference(originAccount.get(), destinationAccount.get(), amount);
//
//        LOGGER.info("[END] - transfer(Account)");
//        return transferenceRepository.save(transference);
//
//    }
}
