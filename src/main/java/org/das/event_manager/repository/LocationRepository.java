package org.das.event_manager.repository;

import org.das.event_manager.domain.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    boolean existsByName(String name);
    boolean existsByAddress(String address);
    boolean existsById(Long id);

    @Query("SELECT l.capacity FROM LocationEntity l WHERE l.id = :id")
    Optional<Integer> getCapacityById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("""
            UPDATE LocationEntity l
            SET
                    l.name = :name,
                    l.address = :address,
                    l.capacity = :capacity,
                    l.description = :description
            WHERE
                    l.id = :id
            """)
    void update(
            @Param("name") String name,
            @Param("address") String address,
            @Param("capacity") Integer capacity,
            @Param("description") String description
    );
}
