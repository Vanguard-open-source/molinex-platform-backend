package com.molinex.platform.production.interfaces.acl;

import java.time.Instant;

public record ProductionRecordSnapshot(Long id, Long batchId, Instant recordedAt) {
}
