package com.tgfcodes.tgfdelivery.delivery.tracking.api.controller;

import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.DeliveryInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.api.output.DeliveryResponse;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.DeliveryCommandService;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.service.DeliveryQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryResponse> draft(@RequestBody @Valid DeliveryInput deliveryInput) {

        Delivery delivery = deliveryCommandService.draft(deliveryInput);
        DeliveryResponse deliveryResponse = DeliveryResponse.toResponse(delivery);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{deliveryId}")
                .buildAndExpand(deliveryResponse.id())
                .toUri();

        log.info("Delivery created with ID: {}", deliveryResponse.id());
        return ResponseEntity.created(location).body(deliveryResponse);
    }

    @PutMapping(value = "/{deliveryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryResponse> edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput deliveryInput) {
        Delivery delivery = deliveryCommandService.edit(deliveryId, deliveryInput);
        DeliveryResponse deliveryResponse = DeliveryResponse.toResponse(delivery);

        log.info("Delivery edited with ID: {}", deliveryResponse.id());
        return ResponseEntity.ok().body(deliveryResponse);
    }

    @GetMapping(value = "/{deliveryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryResponse> get(@PathVariable UUID deliveryId) {
        Delivery delivery = deliveryQueryService.findById(deliveryId);
        DeliveryResponse deliveryResponse = DeliveryResponse.toResponse(delivery);

        log.info("Delivery retrieved with ID: {}", deliveryResponse.id());
        return ResponseEntity.ok(deliveryResponse);
    }
}