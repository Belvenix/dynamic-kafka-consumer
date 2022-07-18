package com.belvenix.dynamickafkaconsumer.exception;

public class NotFoundException extends RuntimeException {
    private static final String MSG = "Consumer with id %s is not found";

    public NotFoundException(String consumerId) {
        super(String.format(MSG, consumerId));
    }
}
