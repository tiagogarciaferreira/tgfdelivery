package com.tgfcodes.tgfdelivery.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Resilience4jCircuitBreakerEventConsumer implements RegistryEventConsumer<CircuitBreaker> {

    private static final Logger log = LoggerFactory.getLogger(Resilience4jCircuitBreakerEventConsumer.class);

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