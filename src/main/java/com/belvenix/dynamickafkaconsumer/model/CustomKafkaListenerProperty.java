package com.belvenix.dynamickafkaconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomKafkaListenerProperty {

    private String topicSuffix;
    private String listenerClass;
}
