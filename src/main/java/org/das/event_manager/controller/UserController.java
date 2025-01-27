package org.das.event_manager.controller;

import jakarta.validation.Valid;
import org.das.event_manager.dto.JwtResponse;
import org.das.event_manager.dto.SignInRequest;
import org.das.event_manager.dto.SignUpRequest;
import org.das.event_manager.dto.UserDto;
import org.das.event_manager.mappers.UserDtoMapper;
import org.das.event_manager.service.AuthenticationService;
import org.das.event_manager.service.UserRegistrationService;
import org.das.event_manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final AuthenticationService authenticationService;
    private final UserDtoMapper userDtoMapper;
    private final UserRegistrationService userRegistrationService;

    public UserController(
            AuthenticationService authenticationService,
            UserDtoMapper userDtoMapper,
            UserRegistrationService userRegistrationService) {
        this.authenticationService = authenticationService;
        this.userDtoMapper = userDtoMapper;
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        LOGGER.info("Post request for SignUp: login = {}", signUpRequest.login());
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(userDtoMapper.toDto(userRegistrationService.register(userDtoMapper.toDomain(signUpRequest))));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authentication(@Valid @RequestBody SignInRequest signInRequest) {
        LOGGER.info("Post request for SignIn: login = {}", signInRequest.login());
        return ResponseEntity.ok().body(new JwtResponse(authenticationService.authenticateUser(signInRequest)));
    }
}
