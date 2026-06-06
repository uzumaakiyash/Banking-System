package com.yash.bank.banking_transfer_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.bank.banking_transfer_system.Service.AccountService;
import com.yash.bank.banking_transfer_system.dto.AccountResponse;
import com.yash.bank.banking_transfer_system.dto.CreateAccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void createAccountTest() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountType("SAVINGS");
        request.setCurrency("INR");

        AccountResponse response = AccountResponse.builder()
                .accountNumber("123456789012")
                .balance(BigDecimal.ZERO)
                .currency("INR")
                .accountType("SAVINGS")
                .build();

        doReturn(response).when(accountService).createAccount(any(CreateAccountRequest.class));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.accountNumber").value("123456789012"));
    }

    @Test
    void getMyAccountsTest() throws Exception {
        List<AccountResponse> accounts = List.of(
                AccountResponse.builder().accountNumber("111111111111").build(),
                AccountResponse.builder().accountNumber("222222222222").build()
        );
        doReturn(accounts).when(accountService).getMyAccounts();

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].accountNumber").value("111111111111"));
    }

    @Test
    void getAccountByNumberTest() throws Exception {
        AccountResponse response = AccountResponse.builder()
                .accountNumber("123456789012")
                .balance(BigDecimal.valueOf(5000))
                .build();

        doReturn(response).when(accountService).getAccountByNumber("123456789012");

        mockMvc.perform(get("/api/accounts/123456789012"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.accountNumber").value("123456789012"));
    }
}