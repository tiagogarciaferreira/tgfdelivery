package com.tgfcodes.tgfdelivery.delivery.tracking.domain.model;

import com.tgfcodes.tgfdelivery.delivery.tracking.domain.Delivery;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.DeliveryStatus;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.Item;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.DeliveryItemNotFoundException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.IncompleteDeliveryException;
import com.tgfcodes.tgfdelivery.delivery.tracking.domain.exception.InvalidDeliveryStateException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class DeliveryTest {

    @Nested
    class DraftDelivery {
        @Test
        void givenNothing_whenDrafting_shouldCreateDeliveryInDraftStatusWithZeroedCosts() {
            var result = Delivery.draft();
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(result.getId()).isNotNull();
                soft.assertThat(result.getStatus()).isEqualTo(DeliveryStatus.DRAFT);
                soft.assertThat(result.getTotalItems()).isZero();
                soft.assertThat(result.getTotalCost()).isEqualTo(BigDecimal.ZERO);
                soft.assertThat(result.getDistanceFee()).isEqualTo(BigDecimal.ZERO);
                soft.assertThat(result.getCourierPayout()).isEqualTo(BigDecimal.ZERO);
                soft.assertThat(result.getItems()).isEmpty();
            });
        }
    }

    @Nested
    class AddItem {
        @Test
        void givenValidItemData_whenAddingItem_shouldStoreItemAndRecalculateTotal() {
            var delivery = DeliveryTestDataBuilder.draftDelivery();
            var itemName = "Lembas Bread";
            var quantity = 5;
            var resultId = delivery.addItem(itemName, quantity);

            assertThat(resultId).isNotNull();
            assertThat(delivery.getItems())
                    .hasSize(1)
                    .first()
                    .satisfies(item -> {
                        assertThat(item.getId()).isEqualTo(resultId);
                        assertThat(item.getName()).isEqualTo(itemName);
                        assertThat(item.getQuantity()).isEqualTo(quantity);
                    });
            assertThat(delivery.getTotalItems()).isEqualTo(quantity);
        }
    }

    @Nested
    class RemoveItem {
        @Test
        void givenExistingItem_whenRemovingItem_shouldClearItemAndRecalculateTotal() {
            var delivery = DeliveryTestDataBuilder.draftDelivery();
            var itemId = delivery.addItem("Mithril Coat", 1);
            delivery.addItem("One Ring", 1);
            delivery.removeItem(itemId);

            assertThat(delivery.getItems())
                    .hasSize(1)
                    .extracting(Item::getName)
                    .containsExactly("One Ring");
            assertThat(delivery.getTotalItems()).isEqualTo(1);
        }
    }

    @Nested
    class ChangeItemQuantity {
        @Test
        void givenExistingItemId_whenChangingQuantity_shouldUpdateQuantityAndTotalItems() {
            var delivery = DeliveryTestDataBuilder.draftDelivery();
            var itemId = delivery.addItem("Lembas Bread", 10);
            var newQuantity = 20;
            delivery.changeItemQuantity(itemId, newQuantity);

            assertThat(delivery.getItems())
                    .first()
                    .extracting(Item::getQuantity)
                    .isEqualTo(newQuantity);
            assertThat(delivery.getTotalItems()).isEqualTo(newQuantity);
        }

        @Test
        void givenNonExistentItemId_whenChangingQuantity_shouldThrowException() {
            var delivery = DeliveryTestDataBuilder.draftDelivery();
            var nonExistentItemId = UUID.randomUUID();
            assertThatExceptionOfType(DeliveryItemNotFoundException.class)
                    .isThrownBy(() -> delivery.changeItemQuantity(nonExistentItemId, 5));
        }
    }

    @Nested
    class EditPreparationDetails {
        @Test
        void givenDraftStatusAndValidDetails_whenEditingPreparation_shouldUpdateValuesAndTotalCost() {
            var preparationDetails = DeliveryTestDataBuilder.aPreparationDetails();
            var delivery = DeliveryTestDataBuilder.draftDelivery();
            BigDecimal totalCost = preparationDetails.getDistanceFee().add(preparationDetails.getCourierPayout());
            delivery.editPreparationDetails(preparationDetails);

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(delivery.getSender()).isEqualTo(preparationDetails.getSender());
                soft.assertThat(delivery.getRecipient()).isEqualTo(preparationDetails.getRecipient());
                soft.assertThat(delivery.getDistanceFee()).isEqualTo(preparationDetails.getDistanceFee());
                soft.assertThat(delivery.getCourierPayout()).isEqualTo(preparationDetails.getCourierPayout());
                soft.assertThat(delivery.getTotalCost()).isEqualTo(totalCost);
                soft.assertThat(delivery.getExpectedDeliveryAt()).isNotNull();
            });
        }

        @Test
        void givenNonDraftStatus_whenEditingPreparation_shouldThrowException() {
            var delivery = DeliveryTestDataBuilder.deliveryWaitingForCourier();
            var preparationDetails = DeliveryTestDataBuilder.aPreparationDetails();
            assertThatExceptionOfType(InvalidDeliveryStateException.class)
                    .isThrownBy(() -> delivery.editPreparationDetails(preparationDetails));
        }
    }

    @Nested
    class Place {
        @Test
        void givenValidFilledDraft_whenPlacing_shouldChangeStatusToWaitingForCourier() {
            var delivery = DeliveryTestDataBuilder.filledDraftDelivery();
            delivery.place();
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.WAITING_FOR_COURIER);
            assertThat(delivery.getPlacedAt()).isNotNull();
        }

        @Test
        void givenIncompleteDraft_whenPlacing_shouldThrowException() {
            var delivery = DeliveryTestDataBuilder.draftDelivery();
            assertThatExceptionOfType(IncompleteDeliveryException.class).isThrownBy(delivery::place);
        }
    }

    @Nested
    class Pickup {
        @Test
        void givenWaitingForCourierStatus_whenPickingUp_shouldAssignCourierAndChangeToInTransit() {
            var delivery = DeliveryTestDataBuilder.deliveryWaitingForCourier();
            delivery.pickup(DeliveryTestDataBuilder.COURIER_ID);
            assertThat(delivery.getCourierId()).isEqualTo(DeliveryTestDataBuilder.COURIER_ID);
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.IN_TRANSIT);
            assertThat(delivery.getAssignedAt()).isNotNull();
        }
    }

    @Nested
    class MarkAsDelivered {
        @Test
        void givenInTransitStatus_whenMarkingAsDelivered_shouldChangeStatusToDelivered() {
            var delivery = DeliveryTestDataBuilder.deliveryInTransit();
            delivery.markAsDelivered();
            assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
            assertThat(delivery.getDeliveredAt()).isNotNull();
            assertThat(delivery.getFulfilledAt()).isNotNull();
        }
    }

    @Nested
    class ClearItems {
        @Test
        void givenDeliveryWithItems_whenClearing_shouldRemoveAllItemsAndZeroTotal() {
            var delivery = DeliveryTestDataBuilder.draftDelivery();
            delivery.addItem("Lembas Bread", 5);
            delivery.addItem("Elven Cloak", 2);
            delivery.clearItems();
            assertThat(delivery.getItems()).isEmpty();
            assertThat(delivery.getTotalItems()).isZero();
        }
    }
}