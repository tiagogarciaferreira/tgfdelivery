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
    }

    @Nested
    class UpdateCourier {

        @Test
        void givenValidInput_whenUpdatingCourier_shouldReturnOkAndCourierOutput() {
            var courierInput = CourierTestDataBuilder.validCourierInput();
            var updatedCourier = CourierTestDataBuilder.samwiseCourier();

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
                    .extracting(CourierOutput::id, CourierOutput::name)
                    .containsExactly(updatedCourier.getId(), updatedCourier.getName());
        }
    }

    @Nested
    class GetCourier {

        @Test
        void givenExistingCourierId_whenGettingCourier_shouldReturnOkAndCourierOutput() {
            var expectedCourier = CourierTestDataBuilder.samwiseCourier();
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
                    .extracting(CourierOutput::id, CourierOutput::name)
                    .containsExactly(expectedCourier.getId(), expectedCourier.getName());
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
            var expectedFee = CourierTestDataBuilder.expectedPayoutFee();

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
    }
}