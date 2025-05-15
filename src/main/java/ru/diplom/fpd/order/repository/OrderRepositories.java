package ru.diplom.fpd.order.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.order.model.Order;
import ru.diplom.fpd.order.model.OrderStatus;


public interface OrderRepositories extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(long id);

    List<Order> findAllByCourierId(long id);

    List<Order> findAllByRestaurantId(long id);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    Optional<Order> findByCourierIdAndOrderStatusIsNotIn(Long courierId, Collection<OrderStatus> orderStatus);

    List<Order> findAllByRestaurantIdAndOrderStatusIsNotIn(Long restaurantId, Collection<OrderStatus> orderStatus);

}
