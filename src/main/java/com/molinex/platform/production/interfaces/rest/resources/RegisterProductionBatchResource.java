package com.molinex.platform.production.interfaces.rest.resources;

import java.time.Instant;

public record RegisterProductionBatchResource(String code, Long receptionId, Instant registeredAt) {
}
