package ru.diplom.fpd.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.order.model.AverageDeliveryTime;

public interface AverageDeliveryTimeRepository extends JpaRepository<AverageDeliveryTime, Long> {
}