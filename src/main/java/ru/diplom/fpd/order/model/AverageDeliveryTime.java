package ru.diplom.fpd.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "average_delivery_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AverageDeliveryTime {

    @Id
    @Column(nullable = false, unique = true)
    private Long restaurantId;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Short deliveryTime;
}
