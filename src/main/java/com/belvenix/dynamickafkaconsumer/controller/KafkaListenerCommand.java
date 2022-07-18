package com.belvenix.dynamickafkaconsumer.controller;

import lombok.Data;

@Data
public class KafkaListenerCommand {
    private String companyCode;
    private String topic;
    private String listenerClass;
    private String groupId;
}
