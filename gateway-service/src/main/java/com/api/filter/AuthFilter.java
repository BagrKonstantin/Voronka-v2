package com.api.filter;

import com.api.util.ParseRequest;
import com.api.DTO.ValidationUser;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RouteValidator validator;
    @Autowired
    private RestTemplate template;

    @Value("${server.port}")
    private String port;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthFilter.Config config) throws HttpClientErrorException {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (exchange.getRequest().getPath().toString().startsWith("/auth/admin")) {
                    String token = ParseRequest.getJWTFromHeader(exchange);
                    ValidationUser user = ParseRequest.getUser(template, "http://localhost:" + port + "/auth/get-user-info?token=" + token);
                    if (!"admin".equals(user.getUser().getRole())) {
                        throw new ResponseStatusException(HttpStatusCode.valueOf(403), user.getValidationMessage().getMessage());
                    }
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
