package ru.diplom.fpd.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link ru.diplom.fpd.model.OrderProduct}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProductDto implements Serializable {
    private final long id;
    private final int count;
    private final ProductDto product;
}