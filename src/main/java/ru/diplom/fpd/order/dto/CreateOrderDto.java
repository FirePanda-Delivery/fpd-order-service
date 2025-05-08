package ru.diplom.fpd.order.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CreateOrderDto {

    private String address;

    private Set<OrderProductDto> products;

    private long restaurantId;

    private long userId;

    private String city;

}
