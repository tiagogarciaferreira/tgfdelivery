package com.tgfcodes.tgfdelivery.delivery.tracking.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DeliveryStatusTest {

    @Nested
    class CanChangeTo {
        @ParameterizedTest
        @MethodSource("com.tgfcodes.tgfdelivery.delivery.tracking.domain.DeliveryStatusTest#validTransitions")
        void givenValidTransition_whenCheckingIfCanChange_shouldReturnTrue(DeliveryStatus currentStatus,
                                                                           DeliveryStatus targetStatus) {
            var result = currentStatus.canChangeTo(targetStatus);
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @MethodSource("com.tgfcodes.tgfdelivery.delivery.tracking.domain.DeliveryStatusTest#invalidTransitions")
        void givenInvalidTransition_whenCheckingIfCanChange_shouldReturnFalse(DeliveryStatus currentStatus,
                                                                              DeliveryStatus targetStatus) {
            var result = currentStatus.canChangeTo(targetStatus);
            assertThat(result).isFalse();
        }
    }

    @Nested
    class CanNotChangeTo {
        @ParameterizedTest
        @MethodSource("com.tgfcodes.tgfdelivery.delivery.tracking.domain.DeliveryStatusTest#invalidTransitions")
        void givenInvalidTransition_whenCheckingIfCanNotChange_shouldReturnTrue(DeliveryStatus currentStatus,
                                                                                DeliveryStatus targetStatus) {
            var result = currentStatus.canNotChangeTo(targetStatus);
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @MethodSource("com.tgfcodes.tgfdelivery.delivery.tracking.domain.DeliveryStatusTest#validTransitions")
        void givenValidTransition_whenCheckingIfCanNotChange_shouldReturnFalse(DeliveryStatus currentStatus,
                                                                               DeliveryStatus targetStatus) {
            var result = currentStatus.canNotChangeTo(targetStatus);
            assertThat(result).isFalse();
        }
    }

    private static Stream<Arguments> validTransitions() {
        return Stream.of(
                Arguments.of(DeliveryStatus.DRAFT, DeliveryStatus.WAITING_FOR_COURIER),
                Arguments.of(DeliveryStatus.WAITING_FOR_COURIER, DeliveryStatus.IN_TRANSIT),
                Arguments.of(DeliveryStatus.IN_TRANSIT, DeliveryStatus.DELIVERED)
        );
    }

    private static Stream<Arguments> invalidTransitions() {
        return Stream.of(
                Arguments.of(DeliveryStatus.DRAFT, DeliveryStatus.IN_TRANSIT),
                Arguments.of(DeliveryStatus.DRAFT, DeliveryStatus.DELIVERED),
                Arguments.of(DeliveryStatus.WAITING_FOR_COURIER, DeliveryStatus.DRAFT),
                Arguments.of(DeliveryStatus.WAITING_FOR_COURIER, DeliveryStatus.DELIVERED),
                Arguments.of(DeliveryStatus.IN_TRANSIT, DeliveryStatus.DRAFT),
                Arguments.of(DeliveryStatus.IN_TRANSIT, DeliveryStatus.WAITING_FOR_COURIER),
                Arguments.of(DeliveryStatus.DELIVERED, DeliveryStatus.DRAFT),
                Arguments.of(DeliveryStatus.DELIVERED, DeliveryStatus.WAITING_FOR_COURIER),
                Arguments.of(DeliveryStatus.DELIVERED, DeliveryStatus.IN_TRANSIT),
                Arguments.of(DeliveryStatus.DRAFT, DeliveryStatus.DRAFT),
                Arguments.of(DeliveryStatus.WAITING_FOR_COURIER, DeliveryStatus.WAITING_FOR_COURIER),
                Arguments.of(DeliveryStatus.IN_TRANSIT, DeliveryStatus.IN_TRANSIT),
                Arguments.of(DeliveryStatus.DELIVERED, DeliveryStatus.DELIVERED)
        );
    }
}