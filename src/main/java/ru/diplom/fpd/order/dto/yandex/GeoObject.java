package ru.diplom.fpd.order.dto.yandex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoObject {

    private String name;
    private String description;
    private String point = ": { pos\": \"39.218487 51.68683";

}
