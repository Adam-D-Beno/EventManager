package org.das.event_manager.dto;

import org.das.event_manager.domain.EventStatus;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventResponseDto(
     Long id,
     String name,
     Long ownerId,
     Integer maxPlaces,
     Integer occupiedPlaces,
     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
     LocalDateTime date,
     BigDecimal cost,
     Integer duration,
     Long locationId,
     EventStatus status
) {}
