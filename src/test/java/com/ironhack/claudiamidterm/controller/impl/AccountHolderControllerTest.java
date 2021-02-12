package com.ironhack.claudiamidterm.controller.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import javax.validation.constraints.AssertTrue;
import java.io.*;
import java.math.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private TransferenceRepository transferenceRepository;

    @Autowired
    private AccountHolderController accountHolderController;

    @Autowired
    private CheckingAccountController checkingAccountController;

    @Autowired
    private CreditCardController creditCardController;

    @Autowired
    private SavingsController savingsController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    CreditCard creditCard;
    CheckingAccount checkingAccount;
    SavingsAccount savingsAccount;
    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        LocalDate date= LocalDate.of(1986,03,19);
        AccountHolder accountHolder1=new AccountHolder("Pablo","pablo",passwordEncoder.encode("123456"),date ,new Address("pintor sorolla", 16),new Address("pintor sorolla", 16));
        AccountHolder accountHolder2=new AccountHolder("Clara","jajajajaja", passwordEncoder.encode("123456"),date ,new Address("pintor goya", 16), new Address("pintor sorolla", 16));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        AccountHolder accountHolderFound= accountHolderRepository.findByName("Pablo");
        AccountHolder accountHolderFound2= accountHolderRepository.findByName("Clara");
        savingsAccount=savingsController.create(new SavingsAccountDTO(accountHolder2.getId(), 1000.00, "s4ving5", 0.5, 100.00));
        checkingAccount=(CheckingAccount) checkingAccountController.create(new CheckingAccountDTO(accountHolderFound.getId(), accountHolderFound2.getId(),1000.00,"1234"));
        creditCard=creditCardController.create(new CreditCardDTO(0.1, accountHolderFound2.getId(), accountHolderFound.getId(),1000.00,300.00));
    }

    @AfterEach
    void tearDown() {
//        transferenceRepository.deleteAll();
//        checkingAccountRepository.deleteAll();
//        creditCardRepository.deleteAll();
//        accountHolderRepository.deleteAll();

    }

    @Test
    void create() throws Exception {
        LocalDate date= LocalDate.of(1992,03,19);
        AccountHolderDTO accountHolder = new AccountHolderDTO("Arturo","jijijij", "123456",date ,"Calle de la paloma", 14);
        MvcResult result = mockMvc.perform(post("/new/accountHolder").content(objectMapper.writeValueAsString(accountHolder)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Arturo"));
    }

    @Test
    void getAllAccounts() throws Exception {

        CredentialsDTO credentialsDTO=new CredentialsDTO("pablo", "123456");

        org.springframework.security.core.userdetails.User user = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user,null);

        MvcResult result = mockMvc.perform(get("/accounts")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(credentialsDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Pablo"));
    }


    //TODO ESTE TEST HAY QUE CORRERLO SOLO
    @Test
    void getAccountBalance() throws Exception {

        CredentialsDTO credentialsDTO=new CredentialsDTO("pablo", "123456");
        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
       Optional<com.ironhack.claudiamidterm.model.User> user=userRepository.findByUsername("pablo");
       Account account=accountRepository.findOneByPrimaryOwnerId(user.get().getId());

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        MvcResult result = mockMvc.perform(get("/accounts/"+account.getId().toString()+"/check-balance")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(credentialsDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
        assertEquals("{\"amount\":1000.00,\"currency\":\"USD\"}",result.getResponse().getContentAsString());
    }

    @Test
    void getAccountBalance_checking_WithMaintenanceFee() throws Exception {

        CredentialsDTO credentialsDTO=new CredentialsDTO("pablo", "123456");
        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        checkingAccount.setLastMonthlyFee(LocalDate.of(2021,1,10));
        checkingAccountRepository.save(checkingAccount);

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        MvcResult result = mockMvc.perform(get("/accounts/"+checkingAccount.getId().toString()+"/check-balance")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(credentialsDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
        assertEquals("{\"amount\":988.00,\"currency\":\"USD\"}",result.getResponse().getContentAsString());
    }

    @Test
    void getAccountBalance_credit_WithInterestRate() throws Exception {

        CredentialsDTO credentialsDTO=new CredentialsDTO("pablo", "123456");
        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        creditCard.setLastInterestUpdate(LocalDate.of(2021,1,10));
        creditCardRepository.save(creditCard);

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        MvcResult result = mockMvc.perform(get("/accounts/"+creditCard.getId().toString()+"/check-balance")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(credentialsDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
        assertEquals("{\"amount\":1010.00,\"currency\":\"USD\"}",result.getResponse().getContentAsString());
    }

    @Test
    void getAccountBalance_savings_WithInterestRate() throws Exception {

        CredentialsDTO credentialsDTO=new CredentialsDTO("pablo", "123456");
        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        savingsAccount.setLastInterestUpdate(LocalDate.of(2020,1,11));
        savingsAccountRepository.save(savingsAccount);

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        MvcResult result = mockMvc.perform(get("/accounts/"+savingsAccount.getId().toString()+"/check-balance")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(credentialsDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
        assertEquals("{\"amount\":1500.00,\"currency\":\"USD\"}",result.getResponse().getContentAsString());
    }

    @Test
    void transfer_everyThingValid() throws Exception {

        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        Optional<com.ironhack.claudiamidterm.model.User> user=userRepository.findByUsername("pablo");
        Account account=accountRepository.findOneByPrimaryOwnerId(user.get().getId());
        TransferenceDTO transferenceDTO=new TransferenceDTO(account.getId(),creditCard.getPrimaryOwner().getName(), creditCard.getId(),new BigDecimal("200") );

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        MvcResult result = mockMvc.perform(post("/accounts/newTransfer")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(transferenceDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("200.00"));
    }

    @Test
    void transfer_notEnoghFunds() throws Exception {

        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        Optional<com.ironhack.claudiamidterm.model.User> user=userRepository.findByUsername("pablo");
        Account account=accountRepository.findOneByPrimaryOwnerId(user.get().getId());
        TransferenceDTO transferenceDTO=new TransferenceDTO(account.getId(),creditCard.getPrimaryOwner().getName(), creditCard.getId(),new BigDecimal("1200") );

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user,null);

        MvcResult result = mockMvc.perform(post("/accounts/newTransfer")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(transferenceDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andReturn();

    }

    @Test
    void transfer_beyondBalanceButWithCredit() throws Exception {

        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        TransferenceDTO transferenceDTO=new TransferenceDTO(creditCard.getId(),checkingAccount.getPrimaryOwner().getName(), checkingAccount.getId(),new BigDecimal("1200") );

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        MvcResult result = mockMvc.perform(post("/accounts/newTransfer")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(transferenceDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

    }

    @Test
    void transfer_minimumBalance() throws Exception {

        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
       TransferenceDTO transferenceDTO=new TransferenceDTO(checkingAccount.getId(),creditCard.getPrimaryOwner().getName(), creditCard.getId(),new BigDecimal("900") );

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        MvcResult result = mockMvc.perform(post("/accounts/newTransfer")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(transferenceDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

    }

    @Test
    void transfer_moreThanPercent() throws Exception {

        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        Transference transference=new Transference(checkingAccount,creditCard,new Money(new BigDecimal("2")));
        transference.setTransactionDate(LocalDateTime.of(2021,Month.JANUARY,11,12,12,12));
        Transference transference2=new Transference(checkingAccount,creditCard,new Money(new BigDecimal("2")));
        transference2.setTransactionDate(LocalDateTime.of(2021,Month.JANUARY,11,12,12,12));
        Transference transference3=new Transference(checkingAccount,creditCard,new Money(new BigDecimal("2")));
        transference3.setTransactionDate(LocalDateTime.of(2021,Month.JANUARY,11,12,12,12));
        transferenceRepository.save(transference);
        transferenceRepository.save(transference2);
        transferenceRepository.save(transference3);

        TransferenceDTO transferenceDTO=new TransferenceDTO(checkingAccount.getId(),creditCard.getPrimaryOwner().getName(), creditCard.getId(),new BigDecimal("200") );


        MvcResult result = mockMvc.perform(post("/accounts/newTransfer")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(transferenceDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();

    }

    @Test
    void transfer_lessThanASecond() throws Exception {

        org.springframework.security.core.userdetails.User userSec = new User("pablo", "123456", AuthorityUtils.createAuthorityList("ACCOUNT_HOLDER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(userSec,null);

        Transference transference=new Transference(checkingAccount,creditCard,new Money(new BigDecimal("2")));
        transference.setTransactionDate(LocalDateTime.now());
        Transference transference2=new Transference(checkingAccount,creditCard,new Money(new BigDecimal("2")));
        transference2.setTransactionDate(LocalDateTime.now());
        Transference transference3=new Transference(checkingAccount,creditCard,new Money(new BigDecimal("2")));
        transference3.setTransactionDate(LocalDateTime.now());
        transferenceRepository.save(transference);
        transferenceRepository.save(transference2);
        transferenceRepository.save(transference3);
        TransferenceDTO transferenceDTO=new TransferenceDTO(checkingAccount.getId(),creditCard.getPrimaryOwner().getName(), creditCard.getId(),new BigDecimal("1") );

        MvcResult result = mockMvc.perform(post("/accounts/newTransfer")
                .principal(testingAuthenticationToken)
                .content(objectMapper.writeValueAsString(transferenceDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();

    }
}