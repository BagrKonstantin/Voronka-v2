package com.api.dto;

import com.api.util.EStatus;
import lombok.Getter;

@Getter
public class JWTResponse extends Response {
    public JWTResponse(EStatus status, String message, String token) {
        super(status, message);
        this.token = token;
    }
    private final String token;
}
