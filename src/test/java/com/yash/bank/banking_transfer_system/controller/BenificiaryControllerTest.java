package com.yash.bank.banking_transfer_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.bank.banking_transfer_system.Service.BeneficiaryService;
import com.yash.bank.banking_transfer_system.dto.BeneficiaryRequest;
import com.yash.bank.banking_transfer_system.dto.BeneficiaryResponse;
import com.yash.bank.banking_transfer_system.dto.UpdateBeneficiaryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryControllerTest {

    @Mock
    private BeneficiaryService beneficiaryService;

    @InjectMocks
    private BeneficiaryController beneficiaryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beneficiaryController).build();
    }

    @Test
    void addBeneficiaryTest() throws Exception {
        BeneficiaryRequest request = new BeneficiaryRequest();
        request.setBeneficiaryAccountNumber("987654321012");
        request.setBeneficiaryName("Sathish");
        request.setNickname("Bro");

        BeneficiaryResponse response = BeneficiaryResponse.builder()
                .id(1L)
                .beneficiaryAccountNumber("987654321012")
                .beneficiaryName("Sathish")
                .nickname("Bro")
                .build();

        doReturn(response).when(beneficiaryService).addBeneficiary(any(BeneficiaryRequest.class));

        mockMvc.perform(post("/api/beneficiaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.beneficiaryAccountNumber").value("987654321012"));
    }

    @Test
    void getMyBeneficiariesTest() throws Exception {
        List<BeneficiaryResponse> list = List.of(
                BeneficiaryResponse.builder().id(1L).beneficiaryName("Sathish").build()
        );
        doReturn(list).when(beneficiaryService).getMyBeneficiaries();

        mockMvc.perform(get("/api/beneficiaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void deleteBeneficiaryTest() throws Exception {
        doNothing().when(beneficiaryService).deleteBeneficiary(5L);

        mockMvc.perform(delete("/api/beneficiaries/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.message").value("Beneficiary removed"));
    }

    @Test
    void updateBeneficiary_ShouldReturn200() throws Exception {
        UpdateBeneficiaryRequest request = new UpdateBeneficiaryRequest();
        request.setBeneficiaryName("Sathish Muralidaran");
        request.setNickname("Brow");

        BeneficiaryResponse response = BeneficiaryResponse.builder()
                .id(1L)
                .beneficiaryName("Sathish Muralidaran")
                .nickname("Brow")
                .build();

        doReturn(response).when(beneficiaryService).updateBeneficiary(eq(1L), any(UpdateBeneficiaryRequest.class));

        mockMvc.perform(put("/api/beneficiaries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.beneficiaryName").value("Sathish Muralidaran"));
    }
}