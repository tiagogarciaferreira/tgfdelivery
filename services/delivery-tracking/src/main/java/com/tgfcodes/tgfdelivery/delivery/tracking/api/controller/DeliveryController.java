package com.tgfcodes.tgfdelivery.delivery.tracking.api.controller;

import com.tgfcodes.tgfdelivery.delivery.tracking.api.input.DeliveryInput;
import com.tgfcodes.tgfdelivery.delivery.tracking.api.output.DeliveryOutput;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.model.Delivery;
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

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOutput> draft(@RequestBody @Valid DeliveryInput deliveryInput) {
        log.debug("Creating delivery with input: {}", deliveryInput);

        Delivery delivery = deliveryCommandService.draft(deliveryInput);
        DeliveryOutput deliveryOutput = DeliveryOutput.toResponse(delivery);

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
        DeliveryOutput deliveryOutput = DeliveryOutput.toResponse(delivery);

        log.info("Delivery edited with ID: {}", deliveryOutput.id());
        return ResponseEntity.ok().body(deliveryOutput);
    }

    @GetMapping(value = "/{deliveryId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOutput> get(@PathVariable UUID deliveryId) {
        log.debug("Retrieving delivery with ID: {}", deliveryId);

        Delivery delivery = deliveryQueryService.findById(deliveryId);
        DeliveryOutput deliveryOutput = DeliveryOutput.toResponse(delivery);

        log.info("Delivery retrieved with ID: {}", deliveryOutput.id());
        return ResponseEntity.ok(deliveryOutput);
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedModel<DeliveryOutput>> search(@PageableDefault Pageable pageable) {
        log.debug("Searching deliveries with pageable: {}", pageable);

        Page<Delivery> searchedPage = deliveryQueryService.search(pageable);
        PagedModel<DeliveryOutput> pagedModel = new PagedModel<>(searchedPage.map(DeliveryOutput::toResponse));

        log.info("Deliveries retrieved with pageable: {}", pageable);
        return ResponseEntity.ok(pagedModel);
    }
}