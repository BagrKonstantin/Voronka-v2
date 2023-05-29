package com.api.dto;

import com.api.util.EStatus;
import lombok.Getter;

@Getter
public class JWTMessage extends Message{
    public JWTMessage(EStatus status, String message, String token) {
        super(status, message);
        this.token = token;
    }
    private final String token;
}
