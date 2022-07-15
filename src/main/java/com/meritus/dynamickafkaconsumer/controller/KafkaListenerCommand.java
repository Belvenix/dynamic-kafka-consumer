package com.meritus.dynamickafkaconsumer.controller;

import lombok.Data;

@Data
public class KafkaListenerCommand {
    private String topic;
    private String listenerClass;
    private String groupId;
    private boolean startImmediately;
}
