package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.model.Role;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.*;

import java.math.*;
import java.security.*;
import java.time.*;
import java.util.*;

import static com.ironhack.claudiamidterm.utils.Calculos.*;

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
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    TransferenceRepository transferenceRepository ;

    @Autowired
    FraudChecker fraudChecker;

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
        Set<Role> roles=new HashSet<>();
        Role role=new Role(UserRole.ACCOUNT_HOLDER, accountHolder);
        roles.add(role);
        accountHolder.setRoles(roles);
        accountHolderRepository.save(accountHolder);

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


    public Money getAccountBalance(Long id, CredentialsDTO credentials, String username) {
        Optional<User> user=userRepository.findByUsername(username);
        Optional<Account> account=accountRepository.findById(id);


            if(!passwordEncoder.matches(credentials.getPassword(), user.get().getPassword())){
                throw new IllegalArgumentException("Password doesn't match with the credentials");
            }


            if(account.get() instanceof CreditCard){
                Optional<CreditCard> creditcard = creditCardRepository.findById(account.get().getId());
                BigDecimal monthlyInt=creditcard.get().getInterestRate().divide(new BigDecimal("12"), RoundingMode.HALF_UP);
                Integer months=calculateMonths(creditcard.get().getLastInterestUpdate());
                addInterests(months, monthlyInt, creditcard.get());
                creditcard.get().setLastInterestUpdate(LocalDate.now());
            }

            if(account.get() instanceof SavingsAccount) {
                Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findById(account.get().getId());

                if (calculateYears(((SavingsAccount) account.get()).getLastInterestUpdate()) >= 1) {
                    Integer completeYears=calculateYears(((SavingsAccount) account.get()).getLastInterestUpdate());
                    addInterests(completeYears, savingsAccount.get().getInterestRate(), savingsAccount.get());

                    savingsAccount.get().setLastInterestUpdate(savingsAccount.get().getLastInterestUpdate().plus(Period.ofYears(completeYears)));

                    savingsAccountRepository.save(savingsAccount.get());
                }
                if (savingsAccount.get().isBelowMinimumBalance() && savingsAccount.get().getBalance().getAmount().compareTo(savingsAccount.get().getMinimumBalance().getAmount()) > 0) {
                    savingsAccount.get().setBelowMinimumBalance(false);
                    savingsAccountRepository.save(savingsAccount.get());
                }
            }
            if(account.get() instanceof CheckingAccount) {
                Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(account.get().getId());
                Integer months=calculateMonths(checkingAccount.get().getLastMonthlyFee());
                Double balance = checkingAccount.get().getBalance().getAmount().doubleValue() - (months * 12);
                checkingAccount.get().setBalance(new Money(new BigDecimal(balance)));
                checkingAccount.get().setCreatedDate(LocalDate.now());

                if (checkingAccount.get().getMinimumBalance().getAmount().compareTo(checkingAccount.get().getBalance().getAmount()) > 0 && !checkingAccount.get().isBelowMinimumBalance()) {
                    checkingAccount.get().getBalance().decreaseAmount(checkingAccount.get().getPenaltyFee());
                    checkingAccount.get().setBelowMinimumBalance(true);
                }
                checkingAccountRepository.save(checkingAccount.get());
            }

            return account.get().getBalance();

    }


    public Transference transfer(TransferenceDTO transferenceDTO, Principal principal) {

        AccountHolder loggedUser = accountHolderRepository.findByUsername(principal.getName());

        //Buscamos cuenta de origen

        Account originAccount = accountRepository.findById(transferenceDTO.getOriginId()).orElseThrow(
                ()-> new IllegalArgumentException("Not found the origin account"));

        //Comprobamos si la cuenta de origen está frozen

        if(originAccount instanceof CheckingAccount && ((CheckingAccount) originAccount).getStatus()==AccountStatus.FROZEN) {
                throw new IllegalArgumentException("The destination account is frozen");
        }
        if(originAccount instanceof SavingsAccount &&((SavingsAccount) originAccount).getStatus() == AccountStatus.FROZEN){
                throw new IllegalArgumentException("The destination account is frozen");
        }
        if(originAccount instanceof StudentChecking && ((StudentChecking) originAccount).getStatus()==AccountStatus.FROZEN){
            throw new IllegalArgumentException("The destination account is frozen");
        }

        List<Account> userAccounts=accountRepository.findAllByPrimaryOwnerIdOrSecondaryOwnerId(loggedUser.getId(), loggedUser.getId());

        if(!userAccounts.contains(originAccount)){
            throw new IllegalArgumentException("Not the owner of the origin account");
        }

        //Chequeo de fraude

        if(!fraudChecker.moreThanPercent(transferenceDTO) || !fraudChecker.inLessThanOneSecond(transferenceDTO)){
            if(originAccount instanceof SavingsAccount) {
                Optional<SavingsAccount> account = savingsAccountRepository.findById(originAccount.getId());
                account.get().setStatus(AccountStatus.FROZEN);
                savingsAccountRepository.save(account.get());
            }
            if(originAccount instanceof CheckingAccount){
                Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(originAccount.getId());
                checkingAccount.get().setStatus(AccountStatus.FROZEN);
                checkingAccountRepository.save(checkingAccount.get());
            }
            if(originAccount instanceof StudentChecking){
                Optional<StudentChecking> studentCheckingAccount =studentCheckingRepository.findById(originAccount.getId());
                studentCheckingAccount.get().setStatus(AccountStatus.FROZEN);
                studentCheckingRepository.save(studentCheckingAccount.get());
            }

            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "This transference is against the fraud rules in our system");
        }

        //Comprobamos que hay saldo o crédito suficiente

        if(originAccount instanceof CreditCard){
            BigDecimal amountWithCredit=originAccount.getBalance().getAmount().add(((CreditCard) originAccount).getCreditLimit().getAmount());
           if(amountWithCredit.compareTo(transferenceDTO.getAmount())<0){
               throw new ResponseStatusException(HttpStatus.CONFLICT, "There are not enough funds in the account");
           }
        }else{
            if(transferenceDTO.getAmount().compareTo(originAccount.getBalance().getAmount()) > 0 ){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "There are not enough funds in the account");
            }
        }

        //Buscamos la cuenta de destino

        Account destinationAccount = accountRepository.findById(transferenceDTO.getDestinationId()).orElseThrow(
                () -> new IllegalArgumentException("Not found the destination account"));
        Money amount = new Money(transferenceDTO.getAmount());

        //Comprobamos si la cuenta de destino está congelada

        if(destinationAccount instanceof CheckingAccount ) {
            if (((CheckingAccount) destinationAccount).getStatus() == AccountStatus.FROZEN ){
                throw new IllegalArgumentException("The destination account is frozen");
            }
        }else if(destinationAccount instanceof StudentChecking ||destinationAccount instanceof SavingsAccount){
            if (((StudentChecking) destinationAccount).getStatus() == AccountStatus.FROZEN
                    || ((SavingsAccount) destinationAccount).getStatus() == AccountStatus.FROZEN) {
                throw new IllegalArgumentException("The destination account is frozen");
            }
        }

        //Realizamos la transacción del lado del sender

        originAccount.getBalance().decreaseAmount(amount);
        accountRepository.save(originAccount);

        //Aplicamos penalty fee si fuera necesario

        if(originAccount instanceof CheckingAccount
                && originAccount.getBalance().getAmount().compareTo(
                        ((CheckingAccount) originAccount).getMinimumBalance().getAmount())<0){
            originAccount.getBalance().decreaseAmount(originAccount.getPenaltyFee());
            accountRepository.save(originAccount);
        }

        if(originAccount instanceof SavingsAccount
                && originAccount.getBalance().getAmount().compareTo(
                ((SavingsAccount) originAccount).getMinimumBalance().getAmount())<0){
            originAccount.getBalance().decreaseAmount(originAccount.getPenaltyFee());
            accountRepository.save(originAccount);
        }

        //Realizamos la operación del lado del receiver
        destinationAccount.getBalance().increaseAmount(amount);
        accountRepository.save(destinationAccount);

        //Guardamos transferencia
        Transference transference = new Transference(originAccount, destinationAccount, amount);
        transferenceRepository.save(transference);

        return transference;
    }

    public void addInterests(Integer months, BigDecimal interestRate, Account account){
        Integer acum=0;
        while (acum<months){
            Double interest =account.getBalance().getAmount().doubleValue()*interestRate.doubleValue();
            BigDecimal newBalance=new BigDecimal(account.getBalance().getAmount().doubleValue()+interest);
            account.setBalance(new Money(newBalance));
            accountRepository.save(account);
            acum++;
        }
    }

}
