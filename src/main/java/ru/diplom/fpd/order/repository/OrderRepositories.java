package ru.diplom.fpd.order.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.order.model.Order;
import ru.diplom.fpd.order.model.OrderStatus;


public interface OrderRepositories extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserId(long id, Pageable pageable);

    List<Order> findAllByCourierId(long id);

    Page<Order> findAllByRestaurantId(long id, Pageable pageable);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    Optional<Order> findByCourierIdAndOrderStatusIsNotIn(Long courierId, Collection<OrderStatus> orderStatus);

    Page<Order> findAllByRestaurantIdAndOrderStatusIsNotIn(Long restaurantId, Collection<OrderStatus> orderStatus, Pageable pageable);

}
