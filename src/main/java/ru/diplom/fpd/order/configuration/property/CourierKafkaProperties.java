package ru.diplom.fpd.order.configuration.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka.order-service")
public class CourierKafkaProperties {
    private String inTopic;
    private String outTopic;
    private String groupId;
}
