package com.prash.fintrackapi.service;

import com.prash.fintrackapi.dto.AuthRequestDTO;
import com.prash.fintrackapi.dto.AuthResponseDTO;
import com.prash.fintrackapi.model.Role;
import com.prash.fintrackapi.model.User;
import com.prash.fintrackapi.model.UserStatus;
import com.prash.fintrackapi.repository.UserRepository;
import com.prash.fintrackapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Register a new user
    public AuthResponseDTO register(AuthRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email '" + request.getEmail() + "' already exists");
        }

        User user = new User();
        user.setName(request.getName());
user.setEmail(request.getEmail());
user.setPassword(passwordEncoder.encode(request.getPassword()));
user.setRole(request.getRole() != null ? request.getRole() : Role.VIEWER);
        user.setStatus(UserStatus.ACTIVE);

        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());

        return new AuthResponseDTO(token, "Bearer", saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }

    // Login user
    public AuthResponseDTO login(AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponseDTO(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}