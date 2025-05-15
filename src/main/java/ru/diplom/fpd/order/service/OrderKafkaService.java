package ru.diplom.fpd.order.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import static ru.diplom.fpd.order.configuration.KafkaConfig.COURIER_FOUND_LISTENER_FACTORY;
import ru.diplom.fpd.order.configuration.property.CourierKafkaProperties;
import ru.diplom.fpd.order.configuration.property.DictionaryKafkaProperties;
import ru.diplom.fpd.order.dto.AverageDeliveryTimeDto;
import ru.diplom.fpd.order.dto.kafka.CourierFoundMessage;
import ru.diplom.fpd.order.dto.kafka.CourierSearchMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaService {

    private final DictionaryKafkaProperties properties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendDeliveryTimeMessage(List<AverageDeliveryTimeDto> message) {
        log.info("Sending search courier message");
        kafkaTemplate.send(properties.getOutDeliveryTimeTopic(), message);
    }
}
