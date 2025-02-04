package com.ironhack.claudiamidterm.controller.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.io.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private CheckingAccountController checkingAccountController;

    @Autowired
    private CreditCardController creditCardController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        LocalDate date= LocalDate.of(1986,03,19);
        AccountHolder accountHolder1=new AccountHolder("Pablo","pablo",passwordEncoder.encode("123456"),date ,new Address("pintor sorolla", 16),new Address("pintor sorolla", 16));
        AccountHolder accountHolder2=new AccountHolder("Clara","jajajajaja", passwordEncoder.encode("123456"),date ,new Address("pintor goya", 16), new Address("pintor sorolla", 16));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        AccountHolder accountHolderFound= accountHolderRepository.findByName("Pablo");
        AccountHolder accountHolderFound2= accountHolderRepository.findByName("Clara");
        checkingAccountController.create(new CheckingAccountDTO(accountHolderFound.getId(), accountHolderFound2.getId(),1000.00,"1234"));
        creditCardController.create(new CreditCardDTO(0.1, accountHolderFound2.getId(), accountHolderFound.getId(),1000.00,300.00));
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void transferFunds() throws Exception {
//
//        Transference transference= new Transference();
//        MvcResult result = mockMvc.perform(post("/accountHolder/transference").content(objectMapper.writeValueAsString(accountHolder)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
//        assertTrue(result.getResponse().getContentAsString().contains("Arturo"));
//    }
}