package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.math.*;
import java.security.*;
import java.util.*;

@Service
public class ThirdPartyService implements IThirdPartyService {
    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ThirdParty create(ThirdPartyDTO thirdPartyDTO) {
        ThirdParty thirdParty=new ThirdParty();

        thirdParty.setName(thirdPartyDTO.getName());
        thirdParty.setHashKey(passwordEncoder.encode(thirdPartyDTO.getHashedKey()));

        return thirdPartyRepository.save(thirdParty);
    }

    public void send(String key, ThirdPartyMovementDTO thirdPartyMovementDTO, Principal principal) {

        ThirdParty thirdParty=thirdPartyRepository.findByName(thirdPartyMovementDTO.getName());

        if(!passwordEncoder.matches(key,thirdParty.getHashKey())){
            throw new IllegalArgumentException("Password doesn't match the credentials");
        }
//
        Account account=accountRepository.findById(thirdPartyMovementDTO.getAccountId()).orElseThrow(()->new IllegalArgumentException("Cannot find the account id provided"));

        //Comprobar si la cuenta está congelada

        if(account instanceof CheckingAccount ) {
            if (((CheckingAccount) account).getStatus() == AccountStatus.FROZEN ){
                throw new IllegalArgumentException("The destination account is frozen");
            }
        }else if(account instanceof StudentChecking ||account instanceof SavingsAccount){
            if (((StudentChecking) account).getStatus() == AccountStatus.FROZEN
                    || ((SavingsAccount) account).getStatus() == AccountStatus.FROZEN) {
                throw new IllegalArgumentException("The destination account is frozen");
            }
        }

        account.getBalance().increaseAmount(new Money(thirdPartyMovementDTO.getAmount()));
        System.out.println(account.getBalance().getAmount());
        accountRepository.save(account);
    }

    public void receive(String key, ThirdPartyMovementDTO thirdPartyMovementDTO, Principal principal) {
        ThirdParty thirdParty=thirdPartyRepository.findByName(thirdPartyMovementDTO.getName());

        if(!passwordEncoder.matches(key,thirdParty.getHashKey())){
            throw new IllegalArgumentException("Password doesn't match the credentials");
        }
        Account account=accountRepository.findById(thirdPartyMovementDTO.getAccountId()).orElseThrow(()->new IllegalArgumentException("Cannot find the account id provided"));

        //Comprobar si la cuenta está congelada

        if(account instanceof CheckingAccount ) {
            if (((CheckingAccount) account).getStatus() == AccountStatus.FROZEN ){
                throw new IllegalArgumentException("The destination account is frozen");
            }
        }else if(account instanceof StudentChecking ||account instanceof SavingsAccount){
            if (((StudentChecking) account).getStatus() == AccountStatus.FROZEN
                    || ((SavingsAccount) account).getStatus() == AccountStatus.FROZEN) {
                throw new IllegalArgumentException("The destination account is frozen");
            }
        }

        //Comprobamos si hay suficiente dinero
        if(account instanceof CreditCard){
            BigDecimal amountWithCredit=account.getBalance().increaseAmount(((CreditCard) account).getCreditLimit());
            if(amountWithCredit.compareTo(thirdPartyMovementDTO.getAmount())<0){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "There are not enough funds in the account");

            }
        }else{
            if(thirdPartyMovementDTO.getAmount().compareTo(account.getBalance().getAmount()) > 0 ){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "There are not enough funds in the account");
            }
        }

        account.getBalance().decreaseAmount(new Money(thirdPartyMovementDTO.getAmount()));
        accountRepository.save(account);

        //Aplicar penalty fee si fuera necesario
        if(account instanceof CheckingAccount
                && account.getBalance().getAmount().compareTo(
                ((CheckingAccount) account).getMinimumBalance().getAmount())<0){
            account.getBalance().decreaseAmount(account.getPenaltyFee());
            accountRepository.save(account);
        }

        if(account instanceof SavingsAccount
                && account.getBalance().getAmount().compareTo(
                ((SavingsAccount) account).getMinimumBalance().getAmount())<0){
            account.getBalance().decreaseAmount(account.getPenaltyFee());
            accountRepository.save(account);
        }
    }
}
