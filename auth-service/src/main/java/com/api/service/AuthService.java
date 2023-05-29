package com.api.service;

import com.api.request.LoginRequest;
import com.api.request.RegistrationRequest;
import com.api.util.EStatus;
import com.api.dto.JWTResponse;
import com.api.dto.Response;
import com.api.model.User;
import com.api.repository.UserRepository;
import com.api.dto.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final List<String> roles = Arrays.asList("customer", "admin", "chef", "manager");

    @Autowired
    AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public Response saveUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new Response(EStatus.ERROR, "Email is already taken");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return new Response(EStatus.ERROR, "Username is already taken");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRole() == null) {
            user.setRole("customer");
        } else {
            if (roles.stream().anyMatch(r -> r.equals(request.getRole()))) {
                user.setRole(request.getRole());
            } else {
                return new Response(EStatus.ERROR, "Role with such name doesn't exist");
            }
        }
        userRepository.save(user);

        return new Response(EStatus.OK, "User registered successfully");
    }

    public Response login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return new Response(EStatus.FORBIDDEN, "Bad credentials: User with such email doesn't exist");
        }
        String username = user.get().getUsername();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        if (authenticate.isAuthenticated()) {
            return new JWTResponse(EStatus.OK, "Token generated successfully", generateToken(username));
        } else {
            return new Response(EStatus.ERROR, "Invalid access");
        }
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public ValidationResponse validateToken(String token) {
        try {
            jwtService.validateToken(token);
            return new ValidationResponse(true, "JWT is valid");
        } catch (Exception ex) {
            return new ValidationResponse(false, ex.getMessage());
        }
    }
}
