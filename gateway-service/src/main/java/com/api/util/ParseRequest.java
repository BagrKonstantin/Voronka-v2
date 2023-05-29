package com.api.util;

import com.api.dto.ValidationUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

public class ParseRequest {
    private ParseRequest () {
    }

    public static String getJWTFromHeader(ServerWebExchange exchange) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Missing authorization header");
        }
        String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Bearer required");
    }
    public static ValidationUser getUser(RestTemplate template, String url) {
        try {
            return template.getForEntity(url, ValidationUser.class).getBody();
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), ex.getMessage());
        }
    }


}
