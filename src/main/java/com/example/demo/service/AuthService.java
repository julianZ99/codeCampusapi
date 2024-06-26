package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.entity.Role;
import com.example.demo.models.entity.User;
import com.example.demo.models.view.LoginDTO;
import com.example.demo.models.view.RegistrationDTO;
import com.example.demo.repository.RoleRepo;
import com.example.demo.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    private final UserRepo userRepo;

    private final RoleRepo roleRepo;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthService(
        UserRepo userRepo,
        RoleRepo roleRepo,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User signup(RegistrationDTO input) {
        if (userRepo.existsByEmail(input.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (userRepo.existsByDni(input.getDni())) {
            throw new IllegalArgumentException("DNI is already in use");
        }

        User user = new User(
                input.getFirstname(),
                input.getLastname(),
                input.getDni(),
                passwordEncoder.encode(input.getPassword()),
                input.getEmail()
        );

        Role userRole = roleRepo.findByName("USER");
        if (userRole == null) {
            throw new RuntimeException("User role not found");
        }
        user.setRoles(userRole);
        user.toString();
        return userRepo.save(user);
    }

    public User authenticate(LoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepo.findByEmail(input.getEmail())
                .orElseThrow();
    }
}

