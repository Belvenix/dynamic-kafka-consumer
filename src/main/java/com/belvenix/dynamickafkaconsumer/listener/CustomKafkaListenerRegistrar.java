package com.belvenix.dynamickafkaconsumer.listener;

import com.belvenix.dynamickafkaconsumer.configuration.CustomKafkaListenerProperties;
import com.belvenix.dynamickafkaconsumer.model.CustomKafkaListenerProperty;
import com.belvenix.dynamickafkaconsumer.repository.CompanyRepository;
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
    private final CompanyRepository companyRepository;

    private static final String TOPIC_PREFIX = "output";

    @Override
    public void afterPropertiesSet() {
        companyRepository.findAll()
                .forEach(company -> customKafkaListenerProperties.getListeners()
                        .forEach((featureName, property) ->
                                registerCompanyKafkaListener(company.getCompanyKafkaCode(), company.getCompanyKafkaGroup(), property)));
    }

    @SneakyThrows
    public void registerCompanyKafkaListener(String companyCode, String companyGroup, CustomKafkaListenerProperty customKafkaListenerProperty) {

        String listenerClass = customKafkaListenerProperty.getListenerClass();
        CustomMessageListener customMessageListener =
                (CustomMessageListener) beanFactory.getBean(Class.forName(listenerClass));
        String topic = this.buildConsumerTopic(companyCode, customKafkaListenerProperty.getTopicSuffix());
        KafkaListenerEndpoint kafkaListenerEndpoint = customMessageListener
                .createKafkaListenerEndpoint(null, topic, companyGroup);
        kafkaListenerEndpointRegistry
                .registerListenerContainer(kafkaListenerEndpoint, kafkaListenerContainerFactory, true);
    }

    private String buildConsumerTopic(String companyCode, String topicSuffix) {
        return String.join(".", TOPIC_PREFIX, companyCode, topicSuffix);
    }
}