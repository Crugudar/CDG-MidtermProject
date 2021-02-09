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
import java.time.*;
import java.util.*;

import static com.ironhack.claudiamidterm.utils.Calculos.calculateYears;
import static com.ironhack.claudiamidterm.utils.Calculos.calculateMonths;

@Service
public class AccountHolderService implements IAccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

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
            Integer months=calculateMonths(account.get().getCreatedDate());

            account.get().getClass();

            switch (account.get().getClass().toString()){
                case "CreditCard":
                    Optional<CreditCard> creditcard=creditCardRepository.findById(account.get().getId());
                    addInterests(months, creditcard.get().getInterestRate(), creditcard.get());
                    creditcard.get().setCreatedDate(LocalDate.now());

                    break;
                case "SavingsAccount":
                    Optional<SavingsAccount> savingsAccount= savingsAccountRepository.findById(account.get().getId());

                    if(calculateYears(account.get().getCreatedDate()) >= 1) {
                        addInterests(12, savingsAccount.get().getInterestRate(), savingsAccount.get());
                        savingsAccountRepository.save(savingsAccount.get());
                    }
                    if(savingsAccount.get().isBelowMinimumBalance() && savingsAccount.get().getBalance().getAmount().compareTo(savingsAccount.get().getMinimumBalance().getAmount()) > 0){
                        savingsAccount.get().setBelowMinimumBalance(false);
                        savingsAccountRepository.save(savingsAccount.get());
                    }

                    break;
                case "CheckingAccount":
                    Optional<CheckingAccount> checkingAccount= checkingAccountRepository.findById(account.get().getId());
                    Double balance =checkingAccount.get().getBalance().getAmount().doubleValue()-(months*12);
                    checkingAccount.get().setBalance(new Money(new BigDecimal(balance)));
                    checkingAccount.get().setCreatedDate(LocalDate.now());

                    if(checkingAccount.get().getMinimumBalance().getAmount().compareTo(checkingAccount.get().getBalance().getAmount()) > 0 && !checkingAccount.get().isBelowMinimumBalance()){
                        checkingAccount.get().getBalance().decreaseAmount(checkingAccount.get().getPenaltyFee());
                        checkingAccount.get().setBelowMinimumBalance(true);
                    }
                    checkingAccountRepository.save(checkingAccount.get());
                    break;
                case "StudentChecking":
                    break;
                default:
                    throw new IllegalArgumentException("Account is not of a valid kind");
            }





            return account.get().getBalance();
        }catch (Exception e){
            throw new IllegalArgumentException("credentials do not correspond with any user/account");
        }
    }

    public void addInterests(Integer months, BigDecimal interestRate, Account account){
        Integer acum=0;
        while (acum<months){
            Double interest =account.getBalance().getAmount().doubleValue()*interestRate.doubleValue();
            BigDecimal newBalance=new BigDecimal(account.getBalance().getAmount().doubleValue()+interest);
            account.setBalance(new Money(newBalance));
            acum++;
        }
    }

}
