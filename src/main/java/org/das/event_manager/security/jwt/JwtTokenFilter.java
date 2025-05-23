package org.das.event_manager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.User;
import org.das.event_manager.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final UserServiceImpl userServiceImpl;
    private final JwtTokenManager jwtTokenManager;


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
        try {
            login = jwtTokenManager.getLoginFromToken(jwt);
        } catch (Exception e) {
            LOGGER.error("Error while reading jwt", e);

            filterChain.doFilter(request,response);
            return;
        }
        User foundUser = userServiceImpl.findByLogin(login);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                foundUser,
                null,
                List.of(new SimpleGrantedAuthority(foundUser.userRole().name()))
        );
        addSecurityContextHolder(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private void addSecurityContextHolder(UsernamePasswordAuthenticationToken token) {
        LOGGER.info("Execute method addSecurityContextHolder");

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
