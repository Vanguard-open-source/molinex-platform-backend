package com.molinex.platform.shared.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.Instant;

/**
 * Common audit fields; concrete persistence entities own their identifier mapping.
 */
@Getter
@MappedSuperclass
public abstract class AuditableAbstractPersistenceEntity {
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void initializeAuditDates() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    private void updateAuditDate() {
        updatedAt = Instant.now();
    }
}
