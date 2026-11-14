package com.molinex.platform.production.domain.model.commands;

import java.time.Instant;

public record RegisterProductionBatchCommand(String code, Long receptionId, Instant registeredAt) {
}
