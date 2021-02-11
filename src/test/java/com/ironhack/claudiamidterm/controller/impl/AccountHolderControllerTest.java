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
    private TransferenceRepository transferenceRepository;

    @Autowired
    private CheckingAccountController checkingAccountController;

    @Autowired
    private CreditCardController creditCardController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    CreditCard creditCard;
    CheckingAccount checkingAccount;
    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        LocalDate date= LocalDate.of(1986,03,19);
        AccountHolder accountHolder1=new AccountHolder("Pablo","pablo",passwordEncoder.encode("123456"),date ,new Address("pintor sorolla", 16),new Address("pintor sorolla", 16));
        AccountHolder accountHolder2=new AccountHolder("Clara","jajajajaja", passwordEncoder.encode("123456"),date ,new Address("pintor goya", 16), new Address("pintor sorolla", 16));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        AccountHolder accountHolderFound= accountHolderRepository.findByName("Pablo");
        AccountHolder accountHolderFound2= accountHolderRepository.findByName("Clara");
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
}