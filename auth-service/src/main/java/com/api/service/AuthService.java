package com.api.service;

import com.api.request.LoginRequest;
import com.api.request.RegistrationRequest;
import com.api.util.EStatus;
import com.api.util.JWTMessage;
import com.api.util.Message;
import com.api.model.User;
import com.api.repository.UserRepository;
import com.api.util.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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


    public Message saveUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new Message(EStatus.ERROR, "Email is already taken");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return new Message(EStatus.ERROR, "Username is already taken");
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
                return new Message(EStatus.ERROR, "Role with such name doesn't exist");
            }
        }
        userRepository.save(user);

        return new Message(EStatus.OK, "User registered successfully");
    }

    public Message login(LoginRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            return new Message(EStatus.FORBIDDEN, "Bad credentials: User with such email doesn't exist");
        }
        String username = userRepository.findByEmail(request.getEmail()).get().getUsername();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        if (authenticate.isAuthenticated()) {
            return new JWTMessage(EStatus.OK, "Token generated successfully", generateToken(username));
        } else {
            return new Message(EStatus.ERROR, "Invalid access");
        }
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public ValidationMessage validateToken(String token) {
        try {
            jwtService.validateToken(token);
            return new ValidationMessage(true, "JWT is valid");
        } catch (Exception ex) {
            return new ValidationMessage(false, ex.getMessage());
        }
    }

//    public ValidationMessage validateTokenByRole(String token, String role) {
//        ValidationMessage validationMessage = validateToken(token);
//        if (validationMessage.getIsValid()) {
//            if (role.equals(userRepository.findByUsername(jwtService.getUserNameFromJwtToken(token)).get().getRole())) {
//                return new ValidationMessage(true, "JWT is valid");
//            } else {
//                return new ValidationMessage(false, "Token doesn't belong to " + role);
//            }
//        }
//        return new ValidationMessage(false, validationMessage.getMessage());
//    }


}
