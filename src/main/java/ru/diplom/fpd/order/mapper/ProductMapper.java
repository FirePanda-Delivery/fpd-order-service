package ru.diplom.fpd.order.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.diplom.fpd.order.dto.OrderProductDto;
import ru.diplom.fpd.order.model.OrderProduct;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "productId", source = "product.id")
    OrderProduct toOrderEntity(OrderProductDto orderProductDto);

    OrderProductDto toOrderDto(OrderProduct orderProduct);

}