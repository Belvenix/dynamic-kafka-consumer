package com.belvenix.dynamickafkaconsumer.exception;

public class AlreadyStoppedException extends RuntimeException {
    private static final String MSG = "Consumer with id %s is already stopped";

    public AlreadyStoppedException(String consumerId) {
        super(String.format(MSG, consumerId));
    }
}

