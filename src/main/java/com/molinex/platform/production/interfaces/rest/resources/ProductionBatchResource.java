package com.molinex.platform.production.interfaces.rest.resources;

import java.time.Instant;

public record ProductionBatchResource(Long id, String code, Long receptionId, Instant registeredAt) {
}
