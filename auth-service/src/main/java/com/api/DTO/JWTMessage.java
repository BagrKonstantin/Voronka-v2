package com.api.DTO;

import com.api.util.EStatus;

public class JWTMessage extends Message{
    public JWTMessage(EStatus status, String message, String token) {
        super(status, message);
        this.token = token;
    }
    private final String token;

    public String getToken() {
        return token;
    }
}
