package com.belvenix.dynamickafkaconsumer.controller;

import com.belvenix.dynamickafkaconsumer.exception.*;
import com.belvenix.dynamickafkaconsumer.listener.CustomKafkaListenerRegistrar;
import com.belvenix.dynamickafkaconsumer.model.CustomKafkaListenerProperty;
import com.belvenix.dynamickafkaconsumer.model.KafkaConsumerAssignmentResponse;
import com.belvenix.dynamickafkaconsumer.model.KafkaConsumerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/kafka/registry")
public class KafkaConsumerRegistryController {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final CustomKafkaListenerRegistrar customKafkaListenerRegistrar;

    @GetMapping
    public List<KafkaConsumerResponse> getConsumerIds() {
        return kafkaListenerEndpointRegistry.getListenerContainerIds()
                .stream()
                .map(this::createKafkaConsumerResponse)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createConsumer(@RequestBody KafkaListenerCommand command) {
        customKafkaListenerRegistrar.registerCompanyKafkaListener(
                command.getCompanyCode(),
                command.getGroupId(),
                CustomKafkaListenerProperty.builder()
                        .topicSuffix(command.getTopic())
                        .listenerClass(command.getListenerClass())
                        .build());
    }

    @PostMapping(path = "/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void activateConsumer(@RequestParam String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new NotFoundException(consumerId);
        } else if (listenerContainer.isRunning()) {
            throw new AlreadyRunningException(consumerId);
        } else {
            log.info("Running a consumer with id " + consumerId);
            listenerContainer.start();
        }
    }

    @PostMapping(path = "/pause")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void pauseConsumer(@RequestParam String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new NotFoundException(consumerId);
        } else if (!listenerContainer.isRunning()) {
            throw new NotRunningException(consumerId);
        } else if (listenerContainer.isContainerPaused()) {
            throw new AlreadyPausedException(consumerId);
        } else if (listenerContainer.isPauseRequested()) {
            throw new AlreadyRequestedToBePausedException(consumerId);
        } else {
            log.info("Pausing a consumer with id " + consumerId);
            listenerContainer.pause();
        }
    }

    @PostMapping(path = "/resume")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resumeConsumer(@RequestParam String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new NotFoundException(consumerId);
        } else if (!listenerContainer.isRunning()) {
            throw new NotRunningException(consumerId);
        } else if (!listenerContainer.isContainerPaused()) {
            throw new NotPausedException(consumerId);
        } else {
            log.info("Resuming a consumer with id " + consumerId);
            listenerContainer.resume();
        }
    }

    @PostMapping(path = "/deactivate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deactivateConsumer(@RequestParam String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new NotFoundException(consumerId);
        } else if (!listenerContainer.isRunning()) {
            throw new AlreadyStoppedException(consumerId);
        } else {
            log.info("Stopping a consumer with id " + consumerId);
            listenerContainer.stop();
        }
    }

    private KafkaConsumerResponse createKafkaConsumerResponse(String consumerId) {
        MessageListenerContainer listenerContainer =
                kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        return KafkaConsumerResponse.builder()
                .consumerId(consumerId)
                .groupId(listenerContainer.getGroupId())
                .listenerId(listenerContainer.getListenerId())
                .active(listenerContainer.isRunning())
                .assignments(Optional.ofNullable(listenerContainer.getAssignedPartitions())
                        .map(topicPartitions -> topicPartitions.stream()
                                .map(this::createKafkaConsumerAssignmentResponse)
                                .collect(Collectors.toList()))
                        .orElse(null))
                .build();
    }

    private KafkaConsumerAssignmentResponse createKafkaConsumerAssignmentResponse(
            TopicPartition topicPartition) {
        return KafkaConsumerAssignmentResponse.builder()
                .topic(topicPartition.topic())
                .partition(topicPartition.partition())
                .build();
    }
}
