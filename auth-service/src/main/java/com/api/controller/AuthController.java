package com.api.controller;

import com.api.model.User;
import com.api.request.LoginRequest;
import com.api.request.RegistrationRequest;
import com.api.util.EStatus;
import com.api.dto.Response;
import com.api.service.AuthService;
import com.api.service.JwtService;
import com.api.service.UserService;
import com.api.dto.ValidationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    AuthController(AuthService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> addNewUser(@Valid @RequestBody RegistrationRequest request) {
        if ("customer".equals(request.getRole()) || request.getRole() == null) {
            return authService.saveUser(request).wrap();
        } else {
            return new Response(EStatus.FORBIDDEN, "Permission denied").wrap();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> getToken(@Valid @RequestBody LoginRequest request) {
        return authService.login(request).wrap();
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<ValidationUser> ret(@RequestParam("token") String token) {
        ValidationUser validationUser = new ValidationUser();
        validationUser.setValidationResponse(authService.validateToken(token));
        if (Boolean.TRUE.equals(validationUser.getValidationResponse().getIsValid())) {
            Optional<User> user = userService.findByUsername(jwtService.getUserNameFromJwtToken(token));
            if (user.isPresent()) {
                validationUser.setUser(user.get());
                return ResponseEntity.ok(validationUser);
            }
        }
        return ResponseEntity.badRequest().body(validationUser);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException.class)
    public Response handleBadCredentialsExceptions(BadCredentialsException ex) {
        return new Response(EStatus.ERROR, ex.getMessage() + ": Wrong password");
    }
}
