package ru.diplom.fpd.order.dto;

import java.io.Serializable;
import lombok.Value;

/**
 * DTO for {@link ru.diplom.fpd.order.model.AverageDeliveryTime}
 */
@Value
public class AverageDeliveryTimeDto implements Serializable {
    Long restaurantId;
    String city;
    Short deliveryTime;
}