package org.das.event_manager.controller;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.dto.EventResponseDto;
import org.das.event_manager.dto.mappers.EventMapper;
import org.das.event_manager.service.EventRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/registrations")
public class EventRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistrationController.class);
    private final EventRegistrationService eventRegistrationService;
    private final EventMapper eventMapper;


    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registrationUserOnEvent(
            @PathVariable("eventId") Long eventId
    ) {
        LOGGER.info("Http post request for registration user on event id = {}", eventId);
        eventRegistrationService.registerUserOnEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> registrationUserCancelEvent(
            @PathVariable("eventId") Long eventId
    ) {
        LOGGER.info("Http delete request for cancel on registration user on event id = {}", eventId);
        eventRegistrationService.cancelOnRegistration(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> findAllEventsByUserRegistration() {
        LOGGER.info("Http get request for find all registration by user on event");
        return ResponseEntity.ok()
                .body(eventMapper.toDto(eventRegistrationService.findAllEventByUserRegistration()));
    }

}
