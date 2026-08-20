package com.tgfcodes.tgfdelivery.delivery.tracking.api.controller;

import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.CourierIdInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.DeliveryInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.api.output.DeliveryOutput;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.DeliveryCheckpointService;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.DeliveryCommandService;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.DeliveryQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "api/v{version}/deliveries", version = "1")
public class DeliveryController {

    private final DeliveryCommandService deliveryCommandService;

    private final DeliveryQueryService deliveryQueryService;

    private final DeliveryCheckpointService deliveryCheckpointService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOutput> draft(@Valid @RequestBody DeliveryInput deliveryInput) {
        log.debug("Creating delivery with input: {}", deliveryInput);

        Delivery delivery = deliveryCommandService.draft(deliveryInput);
        DeliveryOutput deliveryOutput = DeliveryOutput.toOutput(delivery);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{deliveryId}")
                .buildAndExpand(deliveryOutput.id())
                .toUri();

        log.info("Delivery created with ID: {}", deliveryOutput.id());
        return ResponseEntity.created(location).body(deliveryOutput);
    }

    @PutMapping(value = "/{deliveryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOutput> edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput deliveryInput) {
        log.debug("Editing delivery with ID: {} and input: {}", deliveryId, deliveryInput);

        Delivery delivery = deliveryCommandService.edit(deliveryId, deliveryInput);
        DeliveryOutput deliveryOutput = DeliveryOutput.toOutput(delivery);

        log.info("Delivery edited with ID: {}", deliveryOutput.id());
        return ResponseEntity.ok().body(deliveryOutput);
    }

    @GetMapping(value = "/{deliveryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOutput> get(@PathVariable UUID deliveryId) {
        log.debug("Retrieving delivery with ID: {}", deliveryId);

        Delivery delivery = deliveryQueryService.findById(deliveryId);
        DeliveryOutput deliveryOutput = DeliveryOutput.toOutput(delivery);

        log.info("Delivery retrieved with ID: {}", deliveryOutput.id());
        return ResponseEntity.ok(deliveryOutput);
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedModel<DeliveryOutput>> search(@PageableDefault Pageable pageable) {
        log.debug("Searching deliveries with pageable: {}", pageable);

        Page<Delivery> searchedPage = deliveryQueryService.search(pageable);
        PagedModel<DeliveryOutput> pagedModel = new PagedModel<>(searchedPage.map(DeliveryOutput::toOutput));

        log.info("Deliveries retrieved with pageable: {}", pageable);
        return ResponseEntity.ok(pagedModel);
    }

    @PostMapping(value = "/{deliveryId}/placement", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> place(@PathVariable UUID deliveryId) {
        log.debug("Placing delivery with ID: {}", deliveryId);

        deliveryCheckpointService.place(deliveryId);

        log.info("Delivery placed with ID: {}", deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{deliveryId}/pickups", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> pickup(@PathVariable UUID deliveryId, @Valid @RequestBody CourierIdInput courierIdInput) {
        log.debug("Picking up delivery with ID: {} and courier ID: {}", deliveryId, courierIdInput.courierId());

        deliveryCheckpointService.pickup(deliveryId, courierIdInput.courierId());

        log.info("Delivery picked up with ID: {}", deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{deliveryId}/completion", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> complete(@PathVariable UUID deliveryId) {
        log.debug("Completing delivery with ID: {}", deliveryId);

        deliveryCheckpointService.complete(deliveryId);

        log.info("Delivery completed with ID: {}", deliveryId);
        return ResponseEntity.ok().build();
    }
}