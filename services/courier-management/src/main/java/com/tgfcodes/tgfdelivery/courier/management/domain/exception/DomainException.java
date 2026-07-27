package com.tgfcodes.tgfdelivery.courier.management.domain.exception;

abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
