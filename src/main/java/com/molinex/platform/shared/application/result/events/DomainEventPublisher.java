package com.molinex.platform.shared.application.events;

import com.molinex.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import org.jspecify.annotations.Nullable;

/** Technical application contract for publishing and clearing an aggregate's registered events. */
public interface DomainEventPublisher {
    <T extends AbstractDomainAggregateRoot<T>> void publishAndClearEvents(@Nullable T aggregate);
}
