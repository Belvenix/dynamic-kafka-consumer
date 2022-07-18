package com.belvenix.dynamickafkaconsumer.exception;

public class AlreadyPausedException extends RuntimeException {
    private static final String MSG = "Consumer with id %s is already paused";

    public AlreadyPausedException(String consumerId) {
        super(String.format(MSG, consumerId));
    }
}
