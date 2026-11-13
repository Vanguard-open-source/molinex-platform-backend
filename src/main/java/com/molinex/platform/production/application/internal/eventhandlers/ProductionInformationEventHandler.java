package com.molinex.platform.production.application.internal.eventhandlers;

import com.molinex.platform.production.domain.model.events.ProductionInformationRecorded;
import com.molinex.platform.production.domain.model.events.ProductionInformationUpdated;
import com.molinex.platform.production.interfaces.events.ProductionInformationRecordedIntegrationEvent;
import com.molinex.platform.production.interfaces.events.ProductionInformationUpdatedIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductionInformationEventHandler {

    private final ApplicationEventPublisher publisher;

    public ProductionInformationEventHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @TransactionalEventListener
    public void onRecorded(ProductionInformationRecorded event) {
        publisher.publishEvent(new ProductionInformationRecordedIntegrationEvent(event.id()));
    }

    @TransactionalEventListener
    public void onUpdated(ProductionInformationUpdated event) {
        publisher.publishEvent(new ProductionInformationUpdatedIntegrationEvent(event.id()));
    }
}
