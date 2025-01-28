package org.das.event_manager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.das.event_manager.domain.User;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    public JwtTokenFilter(
            @Lazy UserService userService,
            JwtTokenManager jwtTokenManager
    ) {
        this.userService = userService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        LOGGER.info("Execute doFilterInternal in JwtTokenFilter class");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authorizationHeader.substring(7);
        String login;
        String role;
        try {
            login = jwtTokenManager.getLoginFromToken(jwt);
            role = jwtTokenManager.getRoleFromToken(jwt);
        } catch (Exception e) {
            LOGGER.error("Error while reading jwt", e);
            filterChain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                login,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
        addSecurityContextHolder(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private void addSecurityContextHolder(UsernamePasswordAuthenticationToken token) {
        LOGGER.info("Execute method addSecurityContextHolder");
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
