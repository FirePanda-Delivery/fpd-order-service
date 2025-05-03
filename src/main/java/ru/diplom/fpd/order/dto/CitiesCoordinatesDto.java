package ru.diplom.fpd.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitiesCoordinatesDto extends Coordinates {
    private int index;
}
