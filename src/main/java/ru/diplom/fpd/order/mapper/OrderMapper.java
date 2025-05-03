package ru.diplom.fpd.order.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.diplom.fpd.order.dto.OrderDto;
import ru.diplom.fpd.order.dto.RestaurantAddressDto;
import ru.diplom.fpd.order.model.Order;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

//    Order toEntity(OrderDto orderDto);

    @Mapping(target = "restaurantAddress", source = ".", qualifiedByName = "getRestaurantAddress")
    OrderDto toDto(Order order);

    @Named("getRestaurantAddress")
    default RestaurantAddressDto getRestaurantAddress(Order order) {
        return new RestaurantAddressDto(order.getRestaurantAddress(), order.getCity());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "city", source = "restaurantAddress.city")
    @Mapping(target = "restaurantAddress", source = "restaurantAddress.address")
    Order partialUpdate(OrderDto orderDto, @MappingTarget Order order);
}