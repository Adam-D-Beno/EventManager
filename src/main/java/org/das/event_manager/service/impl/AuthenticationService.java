package org.das.event_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.User;
import org.das.event_manager.dto.SignInRequest;
import org.das.event_manager.security.jwt.JwtTokenManager;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;


    public String authenticateUser(SignInRequest signInRequest) {
        LOGGER.info("Execute method authenticateUser user: login = {} in AuthenticationService class",
                signInRequest.login());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.login(),
                signInRequest.password()
        ));
        User foundUser = userService.findByLogin(signInRequest.login());
        return jwtTokenManager.generateJwtToken(foundUser);
    }

    public User getCurrentAuthenticatedUser() {
        LOGGER.info("Execute method getCurrentAuthenticatedUserOrThrow");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            LOGGER.error("Authenticated user not found");
            throw  new IllegalStateException("Authenticated user not exist in context security");
        }
        return (User) authentication.getPrincipal();
    }
}
