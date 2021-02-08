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
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.io.*;
import java.math.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckingAccountControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        LocalDate date= LocalDate.of(1986,03,19);
        AccountHolder accountHolder1=new AccountHolder("Arturo","jijijij","123456",date ,new Address("pintor sorolla", 16),new Address("pintor sorolla", 16));
        AccountHolder accountHolder2=new AccountHolder("Clara","jajajajaja", "123456",date ,new Address("pintor goya", 16), new Address("pintor sorolla", 16));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));

    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();


    }

    @Test
    void create() throws Exception {
       AccountHolder accountHolder1= accountHolderRepository.findByName("Arturo");
        AccountHolder accountHolder2= accountHolderRepository.findByName("Clara");
        CheckingAccountDTO checkingAccount=new CheckingAccountDTO(accountHolder1.getId(), accountHolder2.getId(),1000.00,"1234");
        MvcResult result = mockMvc.perform(post("/new/checking").content(objectMapper.writeValueAsString(checkingAccount)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Arturo"));
        assertTrue(result.getResponse().getContentAsString().contains("Clara"));
    }

    //TODO crear tests para menores de 24 y con uno y dos owners
}