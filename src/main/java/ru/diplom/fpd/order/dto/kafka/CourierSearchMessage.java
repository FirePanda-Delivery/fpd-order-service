package ru.diplom.fpd.order.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.diplom.fpd.order.dto.OrderDto;
import ru.diplom.fpd.order.dto.RestaurantAddressDto;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierSearchMessage {

    private Long orderId;
    private Long restaurantId;
    private String address;
    private RestaurantAddressDto restaurantAddress;

}
