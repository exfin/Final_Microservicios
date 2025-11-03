package com.users.gateway.filter;

import com.users.gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    // Mapa de roles permitidos por ruta base
    private static final Map<String, List<String>> routeRoles = Map.of(
            "/user", List.of("SUPER_ADMIN"),
            "/report", List.of("DAEMON", "SUPER_ADMIN"),
            "/incentive", List.of("SUPER_ADMIN"),
            "/victim", List.of("DAEMON", "SUPER_ADMIN")
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Request recibido → {}" + path);

        // Dejar libre login y registro
        if (path.startsWith("/user/login") || path.startsWith("/user/register") || path.startsWith("/content")) {
            System.out.println("Ruta pública → acceso permitido sin token");
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Header Authorization ausente o incorrecto");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        System.out.println("Token recibido: {}" + token.substring(0, Math.min(15, token.length())) + "...");

        // Validar token
        if (!jwtUtil.validateToken(token)) {
            System.out.println("Token inválido o expirado");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extraer rol del token
        String userRole = jwtUtil.getRoleFromToken(token);

        System.out.println("Usuario autenticado: {}, Rol: {}"+ userRole);

        // Validar acceso según el rol
        for (var entry : routeRoles.entrySet()) {
            String routePrefix = entry.getKey();
            List<String> allowedRoles = entry.getValue();

            if (path.startsWith(routePrefix)) {
                if (!allowedRoles.contains(userRole)) {
                    System.out.println("Acceso denegado a ruta '{}' para rol '{}'" + routePrefix + userRole);
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                } else {
                    System.out.println("Acceso permitido a ruta '{}' para rol '{}'"+ routePrefix + userRole);
                }
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}



