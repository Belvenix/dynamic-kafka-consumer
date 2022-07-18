package com.belvenix.dynamickafkaconsumer.exception;

public class AlreadyRequestedToBePausedException extends RuntimeException {
    private static final String MSG = "Consumer with id %s is already requested to be stopped";

    public AlreadyRequestedToBePausedException(String consumerId) {
        super(String.format(MSG, consumerId));
    }
}
