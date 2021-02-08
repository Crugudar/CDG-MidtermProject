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

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
    }

    @Test
    void create() throws Exception {
        LocalDate date= LocalDate.of(1992,03,19);
        AccountHolderDTO accountHolder = new AccountHolderDTO("Arturo","jijijij", "123456",date ,"Calle de la paloma", 14);
        MvcResult result = mockMvc.perform(post("/new/accountHolder").content(objectMapper.writeValueAsString(accountHolder)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Arturo"));
    }
}