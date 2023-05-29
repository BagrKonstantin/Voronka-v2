package com.api.controller;

import com.api.request.RegistrationRequest;
import com.api.service.AuthService;
import com.api.dto.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {
    private final AuthService authService;

    @Autowired
    AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> addNewUser(@Valid @RequestBody RegistrationRequest request) {
        return authService.saveUser(request).wrap();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
    }
}
