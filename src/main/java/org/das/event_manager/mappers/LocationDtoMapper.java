package org.das.event_manager.mappers;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.domain.Location;
import org.das.event_manager.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Validated
@Component
public class LocationDtoMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationDtoMapper.class);

    public Location toDomain(@NotNull LocationDto locationDto) {
        LOGGER.info("Execute method toDomain in LocationDtoConverter class, got argument locationDto = {} ",
                locationDto);
        return new Location(
                locationDto.id(),
                locationDto.name(),
                locationDto.address(),
                locationDto.capacity(),
                locationDto.description()
        );
    }

    public LocationDto toDto(@NotNull Location location) {
        LOGGER.info("Execute method toDto in LocationDtoConverter class, got argument location = {} ",
                location);
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public List<LocationDto> toDto(@NotNull @NotEmpty List<Location> locations) {
        if (locations.isEmpty()) {
            return List.of();
        }
        LOGGER.info("Execute method toDto in LocationDtoConverter class, got argument locations = {} ",
                locations);
        return locations.stream()
                .map(this::toDto)
                .toList();
    }
}
