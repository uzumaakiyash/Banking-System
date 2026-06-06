package com.yash.bank.banking_transfer_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.bank.banking_transfer_system.Service.TransferService;
import com.yash.bank.banking_transfer_system.dto.TransactionResponse;
import com.yash.bank.banking_transfer_system.dto.TransferRequest;
import com.yash.bank.banking_transfer_system.dto.TransferResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void transferTest() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("111111111111");
        request.setToAccountNumber("222222222222");
        request.setAmount(BigDecimal.valueOf(500));
        request.setDescription("Test transfer");

        TransferResponse response = TransferResponse.builder()
                .transactionReference("TXN123456")
                .fromAccount("111111111111")
                .toAccount("222222222222")
                .amount(BigDecimal.valueOf(500))
                .status("SUCCESS")
                .build();

        doReturn(response).when(transferService).transfer(any(TransferRequest.class));

        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.transactionReference").value("TXN123456"));
    }

    @Test
    void getHistoryTest() throws Exception {
        Page<TransactionResponse> page = new PageImpl<>(List.of(
                TransactionResponse.builder().transactionReference("TXN1").build(),
                TransactionResponse.builder().transactionReference("TXN2").build()
        ));

        doReturn(page).when(transferService).getTransactionHistory(any(Pageable.class));

        mockMvc.perform(get("/api/transfers/history?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }
}