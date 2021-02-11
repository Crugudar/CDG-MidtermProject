package com.ironhack.claudiamidterm.service.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.ironhack.claudiamidterm.*;
import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.controller.impl.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;

import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.security.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.*;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.test.context.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.time.*;
import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountServiceTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckingAccountController checkingAccountController;

    @Autowired
    private AccountHolderController accountHolderController;

    @Autowired
    private CreditCardController creditCardController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    }

    @AfterEach
    void tearDown() {
//        checkingAccountRepository.deleteAll();
//        creditCardRepository.deleteAll();
//        accountHolderRepository.deleteAll();
//        userRepository.deleteAll();
    }


    @Test
    void updateBalance() throws Exception {
        CredentialsDTO credentialsDTO=new CredentialsDTO("Admin", "soy4dmin");
        Admin admin=adminRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(patch("/admin/update/1/3000")

                .content(objectMapper.writeValueAsString(credentialsDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(
                        user(new CustomUserDetails(admin))
                ))
                .andExpect(status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
        assertTrue(result.getResponse().getContentAsString().contains("3000.00"));
    }


    @Test
    void updateBalance_withUser_notAllowed() throws Exception {
        CredentialsDTO credentialsDTO=new CredentialsDTO("pablo", "123456");
        AccountHolder accountHolder=accountHolderRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(
                patch("/admin/update/1/3000")
                     .content(objectMapper.writeValueAsString(credentialsDTO))
                     .contentType(MediaType.APPLICATION_JSON)
                     .with(
                             user(new CustomUserDetails(accountHolder))
                     )).andExpect(status().isForbidden()).andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }
}