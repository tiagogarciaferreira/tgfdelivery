package com.tgfcodes.tgfdelivery.courier.management.api.controller;

import com.tgfcodes.tgfdelivery.courier.management.api.input.CourierInput;
import com.tgfcodes.tgfdelivery.courier.management.api.output.CourierOutput;
import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;
import com.tgfcodes.tgfdelivery.courier.management.domain.service.CourierCommandService;
import com.tgfcodes.tgfdelivery.courier.management.domain.service.CourierQueryService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(value = "api/v{version}/couriers", version = "1")
public class CourierController {

    private final CourierCommandService courierCommandService;

    private final CourierQueryService courierQueryService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourierOutput> create(@Valid @RequestBody CourierInput courierInput) {
        log.debug("Creating courier with input: {}", courierInput);

        Courier courier = courierCommandService.create(courierInput);
        CourierOutput courierOutput = CourierOutput.toOutput(courier);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{courierId}")
                .buildAndExpand(courierOutput.id())
                .toUri();

        log.info("Courier created with ID: {}", courierOutput.id());
        return ResponseEntity.created(location).body(courierOutput);
    }

    @PutMapping(value = "/{courierId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourierOutput> update(@PathVariable UUID courierId, @Valid @RequestBody CourierInput courierInput) {
        log.debug("Updating courier with ID: {} and input: {}", courierId, courierInput);

        Courier courier = courierCommandService.update(courierId, courierInput);
        CourierOutput courierOutput = CourierOutput.toOutput(courier);

        log.info("Courier updated with ID: {}", courierOutput.id());
        return ResponseEntity.ok().body(courierOutput);
    }

    @GetMapping(value = "/{courierId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourierOutput> get(@PathVariable UUID courierId) {
        log.debug("Retrieving courier with ID: {}", courierId);

        Courier courier = courierQueryService.findById(courierId);
        CourierOutput courierOutput = CourierOutput.toOutput(courier);

        log.info("Courier retrieved with ID: {}", courierOutput.id());
        return ResponseEntity.ok().body(courierOutput);
    }

    @GetMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedModel<CourierOutput>> search(@PageableDefault Pageable pageable) {
        log.debug("Searching couriers with pageable: {}", pageable);

        Page<Courier> searchedPage = courierQueryService.search(pageable);
        PagedModel<CourierOutput> pagedModel = new PagedModel<>(searchedPage.map(CourierOutput::toOutput));

        log.info("Couriers retrieved with pageable: {}", pageable);
        return ResponseEntity.ok(pagedModel);
    }
}