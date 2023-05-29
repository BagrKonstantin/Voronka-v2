package com.api.filter;


import com.api.util.ParseRequest;
import com.api.dto.ValidationUser;
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
public class RestaurantFilter extends AbstractGatewayFilterFactory<RestaurantFilter.Config> {
    private RouteValidator validator;

    @Value("${server.port}")
    private String port;

    @Autowired
    public RestaurantFilter(RouteValidator validator) {
        super(Config.class);
        this.validator = validator;
    }


    @Override
    public GatewayFilter apply(Config config) throws HttpClientErrorException {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = ParseRequest.getJWTFromHeader(exchange);
                ValidationUser user = ParseRequest.getUser(new RestTemplate(), "http://localhost:" + port + "/auth/get-user-info?token=" + authHeader);
                if (exchange.getRequest().getPath().toString().startsWith("/restaurant/api") && !"manager".equals(user.getUser().getRole())) {
                    throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Token doesn't belong to manager");

                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
