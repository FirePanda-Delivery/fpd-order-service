package ru.diplom.fpd.order.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CourierFoundMessage {

    private Long orderId;
    private Long courierId;

}
