package com.belvenix.dynamickafkaconsumer.exception;

public class NotPausedException extends RuntimeException {
    private static final String MSG = "Consumer with id %s is not paused";

    public NotPausedException(String consumerId) {
        super(String.format(MSG, consumerId));
    }
}
