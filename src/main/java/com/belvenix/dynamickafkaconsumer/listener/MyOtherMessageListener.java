package com.belvenix.dynamickafkaconsumer.listener;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class MyOtherMessageListener extends CustomMessageListener {

    @Override
    @SneakyThrows
    public KafkaListenerEndpoint createKafkaListenerEndpoint(String name, String topic, String groupId) {
        MethodKafkaListenerEndpoint<String, String> kafkaListenerEndpoint =
                createDefaultMethodKafkaListenerEndpoint(name, topic, groupId);
        kafkaListenerEndpoint.setBean(new MyOtherMessageListener.MyMessageListener());
        kafkaListenerEndpoint.setMethod(MyOtherMessageListener.MyMessageListener.class.getMethod("onMessage", ConsumerRecord.class));
        return kafkaListenerEndpoint;
    }

    @Slf4j
    private static class MyMessageListener implements MessageListener<String, String> {

        @Override
        public void onMessage(@NonNull ConsumerRecord<String, String> consumerRecord) {
            log.info("My other message listener got a new record: " + consumerRecord);
            CompletableFuture.runAsync(this::sleep)
                    .join();
            log.info("My other message listener done processing record: " + consumerRecord);
        }

        @SneakyThrows
        private void sleep() {
            Thread.sleep(5000);
        }
    }
}
