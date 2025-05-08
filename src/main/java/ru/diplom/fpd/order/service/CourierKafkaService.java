package ru.diplom.fpd.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import static ru.diplom.fpd.order.configuration.KafkaConfig.COURIER_FOUND_LISTENER_FACTORY;
import ru.diplom.fpd.order.configuration.property.CourierKafkaProperties;
import ru.diplom.fpd.order.dto.kafka.CourierFoundMessage;
import ru.diplom.fpd.order.dto.kafka.CourierSearchMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierKafkaService {

    private final CourierKafkaProperties properties;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Lazy
    private final OrderServices orderServices;

    public void sendSearchMessage(CourierSearchMessage message) {
        log.info("Sending search courier message");
        kafkaTemplate.send(properties.getOutTopic(), message);
    }

    @KafkaListener(containerFactory = COURIER_FOUND_LISTENER_FACTORY,
            topics = "${kafka.order-service.in-topic}",
            groupId = "${kafka.order-service.group-id}")
    public void listenSearchedMessage(CourierFoundMessage message) {
        orderServices.addCourier(message);
    }

}
