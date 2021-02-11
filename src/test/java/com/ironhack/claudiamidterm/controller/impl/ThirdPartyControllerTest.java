package com.ironhack.claudiamidterm.controller.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.security.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.io.*;
import java.math.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    @Autowired
    private CheckingAccountController checkingAccountController;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private AccountHolderController accountHolderController;

    @Autowired
    private CreditCardController creditCardController;

    @Autowired
    private AdminRepository adminRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    ThirdParty thirdParty;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        Admin admin=new Admin("Admin","Admin", passwordEncoder.encode("soy4dmin"));
        Set<Role> roles=new HashSet<>();
        Role role=new Role(UserRole.ADMIN, admin);
        roles.add(role);
        admin.setRoles(roles);
        adminRepository.save(admin);

        LocalDate date= LocalDate.of(1986,03,19);
        AccountHolder accountHolder1=accountHolderController.create(new AccountHolderDTO("Pablo", "pablo", "123456",LocalDate.of(1980, 03, 23), "pintor sorolla", 16));
        AccountHolder accountHolder2=accountHolderController.create(new AccountHolderDTO("Clara","jajajajaja", "123456",date ,"pintor goya", 16));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        AccountHolder accountHolderFound= accountHolderRepository.findByName("Pablo");
        AccountHolder accountHolderFound2= accountHolderRepository.findByName("Clara");
        checkingAccountController.create(new CheckingAccountDTO(accountHolderFound.getId(), accountHolderFound2.getId(),1000.00,"1234"));
        creditCardController.create(new CreditCardDTO(0.1, accountHolderFound2.getId(), accountHolderFound.getId(),1000.00,300.00));
       thirdParty =new ThirdParty("moni", passwordEncoder.encode("soy3rd"));
       thirdPartyRepository.save(thirdParty);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() throws Exception {

        ThirdPartyDTO thirdPartyDTO=new ThirdPartyDTO("pepa", "heyy");
        Admin admin=adminRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(
                post("/new/thirdParty")
                        .content(objectMapper.writeValueAsString(thirdPartyDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(
                                user(new CustomUserDetails(admin))
                        )).andExpect(status().isCreated()).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("pepa"));
    }

    @Test
    void sendThirdParty() throws Exception {

        ThirdParty thirdParty=thirdPartyRepository.findAll().get(0);
        CheckingAccount account= checkingAccountRepository.findAll().get(0);
        ThirdPartyMovementDTO thirdPartyMovementDTO=new ThirdPartyMovementDTO(account.getId(), account.getSecretKey(), new BigDecimal("300"),thirdParty.getName(), "soy3rd" );
        MvcResult result = mockMvc.perform(
                patch("/third-party/send")
                        .header("key","soy3rd" )
                        .content(objectMapper.writeValueAsString(thirdPartyMovementDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    void receiveThirdParty() throws Exception {
        ThirdParty thirdParty=thirdPartyRepository.findAll().get(0);
        CheckingAccount account= checkingAccountRepository.findAll().get(0);
        ThirdPartyMovementDTO thirdPartyMovementDTO=new ThirdPartyMovementDTO(account.getId(), account.getSecretKey(), new BigDecimal("300"),thirdParty.getName(), "soy3rd" );
        MvcResult result = mockMvc.perform(
                patch("/third-party/receive")
                        .header("key","soy3rd" )
                        .content(objectMapper.writeValueAsString(thirdPartyMovementDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

    }

    @Test
    void receiveThirdParty_notEnough() throws Exception {
        ThirdParty thirdParty=thirdPartyRepository.findAll().get(0);
        CheckingAccount account= checkingAccountRepository.findAll().get(0);
        ThirdPartyMovementDTO thirdPartyMovementDTO=new ThirdPartyMovementDTO(account.getId(), account.getSecretKey(), new BigDecimal("1100"),thirdParty.getName(), "soy3rd" );
        MvcResult result = mockMvc.perform(
                patch("/third-party/receive")
                        .header("key","soy3rd" )
                        .content(objectMapper.writeValueAsString(thirdPartyMovementDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict()).andReturn();

    }

    @Test
    void receiveThirdParty_beyondMinimumBalance() throws Exception {
        ThirdParty thirdParty=thirdPartyRepository.findAll().get(0);
        CheckingAccount account= checkingAccountRepository.findAll().get(0);
        ThirdPartyMovementDTO thirdPartyMovementDTO=new ThirdPartyMovementDTO(account.getId(), account.getSecretKey(), new BigDecimal("900"),thirdParty.getName(), "soy3rd" );
        MvcResult result = mockMvc.perform(
                patch("/third-party/receive")
                        .header("key","soy3rd" )
                        .content(objectMapper.writeValueAsString(thirdPartyMovementDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

    }
}