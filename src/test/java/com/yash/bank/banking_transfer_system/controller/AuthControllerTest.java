package com.yash.bank.banking_transfer_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.bank.banking_transfer_system.Service.AuthService;
import com.yash.bank.banking_transfer_system.dto.ApiResponse;
import com.yash.bank.banking_transfer_system.dto.AuthResponse;
import com.yash.bank.banking_transfer_system.dto.LoginRequest;
import com.yash.bank.banking_transfer_system.dto.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void signupTest() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("yash@gmail.com");
        request.setPassword("pass123");
        request.setUserName("Yash Vanth");

        ApiResponse<?> mockResponse = ApiResponse.builder()
                .result("Success")
                .message("User registered successfully")
                .build();

        doReturn(mockResponse).when(authService).signup(any(SignupRequest.class));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void loginTest() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("yash@gmail.com.com");
        request.setPassword("pass123");

        AuthResponse authResponse = AuthResponse.builder()
                .token("jwt-token")
                .email("yash@gmail.com")
                .role("ROLE_USER")
                .build();

        ApiResponse<AuthResponse> mockResponse = ApiResponse.<AuthResponse>builder()
                .result("Success")
                .message("Login successful")
                .data(authResponse)
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Success"))
                .andExpect(jsonPath("$.data.token").value("jwt-token"));
    }
}