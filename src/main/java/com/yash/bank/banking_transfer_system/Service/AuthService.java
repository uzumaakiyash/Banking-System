package com.yash.bank.banking_transfer_system.Service;

import com.yash.bank.banking_transfer_system.config.JwtService;
import com.yash.bank.banking_transfer_system.dto.ApiResponse;
import com.yash.bank.banking_transfer_system.dto.AuthResponse;
import com.yash.bank.banking_transfer_system.dto.LoginRequest;
import com.yash.bank.banking_transfer_system.dto.SignupRequest;
import com.yash.bank.banking_transfer_system.entity.User;
import com.yash.bank.banking_transfer_system.enums.Role;
import com.yash.bank.banking_transfer_system.exception.EmailAlreadyExistsException;
import com.yash.bank.banking_transfer_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse<?> signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userName(request.getUserName())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        return new ApiResponse<>("Success", "User registered successfully", null);

    }

    public ApiResponse<AuthResponse> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                token
        );

        return new ApiResponse<>("Success", "Login successful", response);

    }
}
