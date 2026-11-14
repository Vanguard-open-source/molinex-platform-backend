package com.molinex.platform.production.domain.model.queries;

import java.time.Instant;

public record GetProductionHistoryQuery(Instant from, Instant to) {

    public GetProductionHistoryQuery {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Start of period must not follow the end");
        }
    }
}
