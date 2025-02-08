package org.das.event_manager.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_registration")
@Setter
@Getter
public class UserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    public UserRegistration() {
    }

    public UserRegistration(
            Long id,
            UserEntity user,
            EventEntity event
    ) {
        this.id = id;
        this.user = user;
        this.event = event;
    }
}
