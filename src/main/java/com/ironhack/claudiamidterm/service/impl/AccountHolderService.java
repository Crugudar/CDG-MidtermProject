package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.model.Role;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.*;
import java.util.*;

@Service
public class AccountHolderService implements IAccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository ;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AccountHolder create(AccountHolderDTO accountHolderDTO) {
        AccountHolder accountHolder = new AccountHolder();
        accountHolder.setName(accountHolderDTO.getName());

        if(!accountHolderDTO.getUsername().isEmpty()){
            accountHolder.setUsername(accountHolderDTO.getUsername());
        }else{
            accountHolder.setUsername(accountHolderDTO.getName());
        }

        accountHolder.setPassword(passwordEncoder.encode(accountHolderDTO.getPassword()));
        accountHolder.setDateOfBirth(accountHolderDTO.getDateOfBirth());

        if(accountHolderDTO.getMailingAddressDirection()!=null&&accountHolderDTO.getMailingAddressNumber()!=null){
            accountHolder.setPrimaryAddress(new Address(accountHolderDTO.getPrimaryAddressDirection(), accountHolderDTO.getPrimaryAddressNumber()));
            accountHolder.setMailingAddress(new Address(accountHolderDTO.getMailingAddressDirection(), accountHolderDTO.getMailingAddressNumber()));
        }else{
            Address address=new Address(accountHolderDTO.getPrimaryAddressDirection(), accountHolderDTO.getPrimaryAddressNumber());
            accountHolder.setPrimaryAddress(address);
            accountHolder.setMailingAddress(address);
        }

        accountHolder.addRole(new Role(UserRole.ACCOUNT_HOLDER, accountHolder));

        System.out.println("Account Holder created");

        return accountHolderRepository.save(accountHolder);
    }


    public List<Account> getAllAccounts(CredentialsDTO credentials,String username) {
        AccountHolder accountHolder=accountHolderRepository.findByUsername(username);
        try{
            passwordEncoder.matches(credentials.getPassword(), accountHolder.getPassword());
            return accountRepository.findAllByPrimaryOwnerIdOrSecondaryOwnerId(accountHolder.getId(), accountHolder.getId());
        }catch (Exception e){
            throw new IllegalArgumentException("credentials do not correspond with any user");
        }
    }

    @Override
    public Money getAccountBalance(Long id, CredentialsDTO credentials, String username) {
        AccountHolder accountHolder=accountHolderRepository.findByUsername(username);
        try{
            passwordEncoder.matches(credentials.getPassword(), accountHolder.getPassword());
            Optional<Account> account=accountRepository.findById(id);
            return account.get().getBalance();
        }catch (Exception e){
            throw new IllegalArgumentException("credentials do not correspond with any user/account");
        }
    }

}
