package com.tgfcodes.tgfdelivery.courier.management.api;

import com.tgfcodes.tgfdelivery.courier.management.api.output.CourierOutput;
import com.tgfcodes.tgfdelivery.courier.management.api.output.CourierPayoutResultOutput;
import com.tgfcodes.tgfdelivery.courier.management.domain.model.Courier;
import com.tgfcodes.tgfdelivery.courier.management.domain.repository.CourierRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourierControllerIT {

    @Autowired
    private CourierRepository courierRepository;

    @LocalServerPort
    private int port;

    private Courier defaultCourier;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/couriers";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        courierRepository.deleteAll();
        defaultCourier = courierRepository.save(CourierTestDataBuilder.samwiseCourier());
    }

    @Nested
    class CreateCourier {

        @Test
        void givenValidInput_whenCreatingCourier_shouldReturnCreatedAndCourierOutput() {
            var courierInput = CourierTestDataBuilder.validCourierInput();
            var expectedCourier = CourierTestDataBuilder.samwiseCourier();

            var response = given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(courierInput)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", notNullValue())
                    .extract()
                    .as(CourierOutput.class);

            assertThat(response)
                    .isNotNull()
                    .extracting(CourierOutput::name, CourierOutput::phone)
                    .containsExactly(expectedCourier.getName(), expectedCourier.getPhone());
        }

        @Test
        void givenInvalidName_whenCreatingCourier_shouldReturnBadRequest() {
            var invalidCourierInput = CourierTestDataBuilder.invalidCourierInputBlankName();
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(invalidCourierInput)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void givenInvalidPhone_whenCreatingCourier_shouldReturnBadRequest() {
            var invalidCourierInput = CourierTestDataBuilder.invalidCourierInputBlankPhone();
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(invalidCourierInput)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class UpdateCourier {

        @Test
        void givenValidInput_whenUpdatingCourier_shouldReturnOkAndCourierOutput() {
            var courierInput = CourierTestDataBuilder.validCourierInput();

            var response = given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("courierId", defaultCourier.getId())
                    .body(courierInput)
                    .when()
                    .put("/{courierId}", defaultCourier.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(CourierOutput.class);

            assertThat(response)
                    .isNotNull()
                    .satisfies(
                            courierOutput -> assertThat(courierOutput.id()).isNotNull(),
                            courierOutput -> assertThat(courierOutput.name()).isEqualTo(courierInput.name())
                    );
        }

        @Test
        void givenNonExistentCourierId_whenUpdatingCourier_shouldReturnNotFound() {
            var nonExistentCourierId = UUID.randomUUID();
            var courierInput = CourierTestDataBuilder.validCourierInput();

            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("courierId", nonExistentCourierId)
                    .body(courierInput)
                    .when()
                    .put("/{courierId}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void givenInvalidInput_whenUpdatingCourier_shouldReturnBadRequest() {
            var invalidCourierInput = CourierTestDataBuilder.invalidCourierInputBlankName();
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("courierId", defaultCourier.getId())
                    .body(invalidCourierInput)
                    .when()
                    .put("/{courierId}")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetCourier {

        @Test
        void givenExistingCourierId_whenGettingCourier_shouldReturnOkAndCourierOutput() {
            var response = given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("courierId", defaultCourier.getId())
                    .when()
                    .get("/{courierId}", defaultCourier.getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(CourierOutput.class);

            assertThat(response)
                    .isNotNull()
                    .satisfies(
                            courierOutput -> assertThat(courierOutput.id()).isEqualTo(defaultCourier.getId()),
                            courierOutput -> assertThat(courierOutput.name()).isEqualTo(defaultCourier.getName())
                    );
        }

        @Test
        void givenNonExistentCourierId_whenGettingCourier_shouldReturnNotFound() {
            var nonExistentCourierId = UUID.randomUUID();
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .pathParam("courierId", nonExistentCourierId)
                    .when()
                    .get("/{courierId}")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    class SearchCouriers {

        @Test
        void givenPageable_whenSearchingCouriers_shouldReturnOkAndPagedModel() {
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .when()
                    .get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("content[0].id", equalTo(defaultCourier.getId().toString()))
                    .body("content[0].name", equalTo(defaultCourier.getName()))
                    .body("page.totalElements", equalTo(1));
        }
    }

    @Nested
    class CalculatePayout {

        @Test
        void givenValidDistance_whenCalculatingPayout_shouldReturnOkAndFee() {
            var payoutInput = CourierTestDataBuilder.validPayoutInput();
            var expectedFee = BigDecimal.valueOf(155.00).setScale(2, RoundingMode.HALF_EVEN);

            var response = given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(payoutInput)
                    .when()
                    .post("/payout-calculation")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(CourierPayoutResultOutput.class);

            assertThat(response)
                    .isNotNull()
                    .extracting(CourierPayoutResultOutput::payoutFee)
                    .isEqualTo(expectedFee);
        }

        @Test
        void givenInvalidDistance_whenCalculatingPayout_shouldReturnBadRequest() {
            var invalidPayoutInput = CourierTestDataBuilder.invalidPayoutInput();
            given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(invalidPayoutInput)
                    .when()
                    .post("/payout-calculation")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}