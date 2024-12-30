package org.das.event_manager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.das.event_manager.mappers.LocationEntityMapper;
import org.das.event_manager.domain.Location;
import org.das.event_manager.entity.LocationEntity;
import org.das.event_manager.repository.LocationRepository;
import org.das.event_manager.validation.LocationValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service
@Validated
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;
    private final LocationEntityMapper entityMapper;
    private final LocationValidate locationValidate;

    @Autowired
    public LocationService(
            LocationRepository locationRepository,
            LocationEntityMapper entityMapper,
            LocationValidate locationValidate
    ) {
        this.locationRepository = locationRepository;
        this.entityMapper = entityMapper;
        this.locationValidate = locationValidate;
    }

    public List<Location> findAll() {
        LOGGER.info("Execute method findAll in LocationService class");
        return entityMapper.toDomain(locationRepository.findAll());
    }

    public Location create(@NotNull Location locationToUpdate) {
        LOGGER.info("Execute method create in LocationService class, got argument locationToUpdate = {}",
                    locationToUpdate);
        locationValidate.validateLocationIdNull(locationToUpdate.id());
        existLocationName(locationToUpdate.name());
        existLocationAddress(locationToUpdate.address());
        return entityMapper.toDomain(locationRepository.save(entityMapper.toEntity(locationToUpdate)));
    }

    public Location deleteById(@NotNull Long locationId) {
        LOGGER.info("Execute method deleteById in LocationService class, got argument locationId = {}",
                locationId);
        LocationEntity foundLocationEntityForDelete = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("No such found location with id = %s"
                        .formatted(locationId)));
        locationRepository.deleteById(locationId);
        return entityMapper.toDomain(foundLocationEntityForDelete);
    }

    public Location findById(@NotNull Long locationId) {
        LOGGER.info("Execute method findById in LocationService class, got argument locationId = {}",
                locationId);
        return locationRepository.findById(locationId)
                .map(entityMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("No such found location with id = %s"
                        .formatted(locationId)));
    }

    public Location updateById(@NotNull Long locationId, @NotNull Location location) {
        LOGGER.info("Execute method updateById in LocationService class, got arguments locationId = {}, location = {}",
                locationId, location);
        locationValidate.validateLocationIdNull(location.id());
        if (!locationRepository.existsById(locationId)) {
            LOGGER.error("No found location = {} with id = {}",locationId, location);
            throw new EntityNotFoundException(
                    "No location by id=%s".formatted(locationId)
            );
        }
        existLocationName(location.name());
        existLocationAddress(location.address());
        Integer foundCapacity = locationRepository.getCapacityById(locationId);
        if (location.capacity() < foundCapacity) {
            LOGGER.error("Capacity for update = {} should be greater than the existing capacity = {} " +
                            "for location: id = {}, name = {}",
                    location.capacity(), foundCapacity, locationId, location.name());
            throw new IllegalArgumentException(("Capacity for update = %s should be more " +
                    "than the capacity = %s that already exists")
                    .formatted(location.capacity(), foundCapacity));
        }
        LocationEntity updatedEntity = locationRepository.update(
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
        return entityMapper.toDomain(updatedEntity);
    }

    public void existLocationAddress(String locationAddress) {
        LOGGER.info("Execute isExistLocationAddress in LocationService class, location address = {}", locationAddress);
        if (locationRepository.existsByAddress(locationAddress)) {
            LOGGER.info("Location address = {} exist", locationAddress);
            throw new IllegalArgumentException("Location address = %s exist".formatted(locationAddress));
        }
    }

    public void existLocationName(String locationName) {
        LOGGER.info("Execute isExistLocationName in LocationService class, location name = {}", locationName);
        if (locationRepository.existsByName(locationName)) {
            LOGGER.info("Location location name = {} exist", locationName);
            throw new IllegalArgumentException("Location name = %s exist".formatted(locationName));
        }
    }
}
