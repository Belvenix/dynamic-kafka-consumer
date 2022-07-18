package com.belvenix.dynamickafkaconsumer.exception;

public class AlreadyRunningException extends RuntimeException {
    private static final String MSG = "Consumer with id %s is already running";

    public AlreadyRunningException(String consumerId) {
        super(String.format(MSG, consumerId));
    }
}
