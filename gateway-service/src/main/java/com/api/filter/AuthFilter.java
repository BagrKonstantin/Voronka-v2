package com.api.filter;

import com.api.util.ParseRequest;
import com.api.dto.ValidationUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    @Value("${server.port}")
    private String port;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthFilter.Config config) throws HttpClientErrorException {
        return ((exchange, chain) -> {
            if (RouteValidator.isSecured.test(exchange.getRequest()) && exchange.getRequest().getPath().toString().startsWith("/auth/admin")) {
                String token = ParseRequest.getJWTFromHeader(exchange);
                ValidationUser user = ParseRequest.getUser(new RestTemplate(), "http://localhost:" + port + "/auth/get-user-info?token=" + token);
                if (!"admin".equals(user.getUser().getRole())) {
                    throw new ResponseStatusException(HttpStatusCode.valueOf(403), user.getValidationResponse().getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
