package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Resilience4jCircuitBreakerEventConsumer implements RegistryEventConsumer<CircuitBreaker> {

    @Override
    public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
        registerRetryListener(entryAddedEvent.getAddedEntry());
    }

    @Override
    public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
        registerRetryListener(entryRemoveEvent.getRemovedEntry());
    }

    @Override
    public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
        registerRetryListener(entryReplacedEvent.getNewEntry());
        registerRetryListener(entryReplacedEvent.getOldEntry());
    }

    private void registerRetryListener(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher().onEvent(this::logCircuitBreakerEvent);
    }

    private void logCircuitBreakerEvent(CircuitBreakerEvent event) {
        log.info("Circuit Breaker Event [{}]: type={}, creationTime={}",
                event.getCircuitBreakerName(),
                event.getEventType(),
                event.getCreationTime());
    }
}