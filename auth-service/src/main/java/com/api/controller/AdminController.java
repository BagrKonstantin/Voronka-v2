package com.api.controller;

import com.api.request.RegistrationRequest;
import com.api.service.AuthService;
import com.api.util.Message;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {
    private final AuthService authService;

    @Autowired
    AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Message> addNewUser(@Valid @RequestBody RegistrationRequest request) {
        return authService.saveUser(request).wrap();
    }
}
