package ru.diplom.fpd.order.configuration.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka.dictionary-service")
public class DictionaryKafkaProperties {
    private String outDeliveryTimeTopic;
    private String groupId;
}
