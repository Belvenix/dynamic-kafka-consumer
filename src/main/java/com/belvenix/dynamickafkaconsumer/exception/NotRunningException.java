package com.belvenix.dynamickafkaconsumer.exception;

public class NotRunningException extends RuntimeException {
    private static final String MSG = "Consumer with id %s is not running";

    public NotRunningException(String consumerId) {
        super(String.format(MSG, consumerId));
    }
}
