package ru.diplom.fpd.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.diplom.fpd.order.dto.AverageDeliveryTimeDto;
import ru.diplom.fpd.order.model.AverageDeliveryTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AverageDeliveryTimeMapper {
    AverageDeliveryTime toEntity(AverageDeliveryTimeDto averageDeliveryTimeDto);

    AverageDeliveryTimeDto toDto(AverageDeliveryTime averageDeliveryTime);

}