package com.meritus.dynamickafkaconsumer.listener;

import com.meritus.dynamickafkaconsumer.configuration.CustomKafkaListenerProperties;
import com.meritus.dynamickafkaconsumer.model.CustomKafkaListenerProperty;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomKafkaListenerRegistrar implements InitializingBean {

    private final CustomKafkaListenerProperties customKafkaListenerProperties;
    private final BeanFactory beanFactory;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaListenerContainerFactory kafkaListenerContainerFactory;

    @Override
    public void afterPropertiesSet() {
        customKafkaListenerProperties.getListeners()
                .forEach(this::registerCustomKafkaListener);
    }

    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty) {
        this.registerCustomKafkaListener(name, customKafkaListenerProperty, false);
    }

    @SneakyThrows
    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty,
                                            boolean startImmediately) {
        String listenerClass = String.join(".", CustomKafkaListenerRegistrar.class.getPackageName(),
                customKafkaListenerProperty.getListenerClass());
        CustomMessageListener customMessageListener =
                (CustomMessageListener) beanFactory.getBean(Class.forName(listenerClass));
        KafkaListenerEndpoint kafkaListenerEndpoint = customMessageListener
                .createKafkaListenerEndpoint(name, customKafkaListenerProperty.getTopic(), customKafkaListenerProperty.getGroupId() );
        kafkaListenerEndpointRegistry.registerListenerContainer(
                kafkaListenerEndpoint,
                kafkaListenerContainerFactory, startImmediately);
    }
}