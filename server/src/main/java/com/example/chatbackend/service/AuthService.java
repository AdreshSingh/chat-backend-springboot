package com.example.chatbackend.service;

import com.example.chatbackend.model.User;
import com.example.chatbackend.repository.UserRepository;
import com.example.chatbackend.security.JwtUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;

    public AuthResponse register(User request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token, user.getName());
    }

    public AuthResponse login(User request) {
        authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtils.generateToken(user);
        return new AuthResponse(token, user.getName());
    }
}
