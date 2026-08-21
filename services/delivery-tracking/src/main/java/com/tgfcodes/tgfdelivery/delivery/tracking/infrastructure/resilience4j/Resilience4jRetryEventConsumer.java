package com.tgfcodes.tgfdelivery.delivery.tracking.infrastructure.resilience4j;

import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.event.RetryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Resilience4jRetryEventConsumer implements RegistryEventConsumer<Retry> {

    @Override
    public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
        registerRetryListener(entryAddedEvent.getAddedEntry());
    }

    @Override
    public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
        registerRetryListener(entryRemoveEvent.getRemovedEntry());
    }

    @Override
    public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
        registerRetryListener(entryReplacedEvent.getNewEntry());
        registerRetryListener(entryReplacedEvent.getOldEntry());
    }

    private void registerRetryListener(Retry retry) {
        retry.getEventPublisher().onEvent(this::logRetryEvent);
    }

    private void logRetryEvent(RetryEvent event) {
        log.info("Retry Event [{}]: type={}, creationTime={}",
                event.getName(),
                event.getEventType(),
                event.getCreationTime());
    }
}