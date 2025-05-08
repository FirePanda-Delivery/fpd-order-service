package ru.diplom.fpd.order.service;

import jakarta.persistence.EntityNotFoundException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import ru.diplom.fpd.order.dto.CreateOrderDto;
import ru.diplom.fpd.order.dto.OrderDto;
import ru.diplom.fpd.order.dto.RestaurantAddressDto;
import ru.diplom.fpd.order.dto.RestaurantDto;
import ru.diplom.fpd.order.dto.kafka.CourierFoundMessage;
import ru.diplom.fpd.order.dto.kafka.CourierSearchMessage;
import ru.diplom.fpd.order.exception.AddressNotInDeliveryAreaException;
import ru.diplom.fpd.order.feign.RestaurantApi;
import ru.diplom.fpd.order.mapper.OrderMapper;
import ru.diplom.fpd.order.mapper.ProductMapper;
import ru.diplom.fpd.order.model.Order;
import ru.diplom.fpd.order.model.OrderProduct;
import ru.diplom.fpd.order.model.OrderStatus;
import ru.diplom.fpd.order.processing.AddressProcessing;
import ru.diplom.fpd.order.repository.OrderRepositories;

@Service
@AllArgsConstructor
public class OrderServices {

    private final OrderRepositories orderRepositories;

    @Lazy
    private final CourierKafkaService courierKafkaService;
    private final RestaurantApi restaurantApi;
    private final AddressProcessing addressProcessing;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    private final List<OrderStatus> FINAL_STATUSES = Arrays.asList(OrderStatus.DELIVERED, OrderStatus.CANCELED);
    private final Validator validator;


    public OrderDto getOrder(long id) {
        return orderMapper.toDto(orderRepositories.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public List<OrderDto> getUserOrders(long userId) {
        return orderRepositories.findAllByUserId(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

//    public List<OrderDto> getCourierOrders(long id) {
//        return orderRepositories.findAllByCourierId(id).stream()
//                .map(orderMapper::toDto)
//                .toList();
//    }

//    public Order getActiveCourierOrder(long courierId) {
//
//        Order order = Storage.courierActiveOrder.get(courierId);
//
//        if (order != null) {
//            return order;
//        }
//
//        Optional<Order> orderOptional = orderRepositories.findByCourier_IdAndOrderStatusIsNotIn(courierId, FINAL_STATUSES);
//
//        if (orderOptional.isEmpty()) {
//            throw new EntityNotFoundException("order not found");
//        }
//
//        order = orderOptional.get();
//
//        Storage.courierActiveOrder.put(courierId, order);
//
//        return order;
//    }

    public List<OrderDto> getActiveRestaurantOrder(long restaurantId) {

            List<Order> orders = orderRepositories.findAllByRestaurantIdAndOrderStatusIsNotIn(restaurantId, FINAL_STATUSES);
            return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    public List<OrderDto> getRestaurantOrders(long id) {
        return orderRepositories.findAllByRestaurantId(id).stream()
                .map(orderMapper::toDto)
                .toList();
    }


    public void addCourier(CourierFoundMessage message) {

        Order order = orderRepositories.findById(message.getOrderId()).orElseThrow(EntityNotFoundException::new);
        order.setCourierId(message.getCourierId());
        orderRepositories.save(order);
    }

    public OrderDto createOrder(CreateOrderDto createOrderDto) {

        validateOrder(createOrderDto);

        Set<OrderProduct> orderProducts = createOrderDto.getProducts().stream()
                .map(productMapper::toOrderEntity)
                .collect(Collectors.toSet());

        RestaurantDto restaurant = restaurantApi.getRestaurant(createOrderDto.getRestaurantId()).getBody();
        String city = createOrderDto.getCity();
        String address = createOrderDto.getAddress();

        RestaurantAddressDto restaurantAddress = addressProcessing.restaurantNearestToAddress(restaurant, city, address);

        Order order = Order.builder()
                .address(address)
                .userId(createOrderDto.getUserId())
                .restaurantId(createOrderDto.getRestaurantId())
                .orderStatus(OrderStatus.CREATED)
                .date(new Date())
                .productList(orderProducts)
                .timeStart(new Time(new Date().getTime()))
                .restaurantAddress(restaurantAddress.getAddress())
                .city(city)
                .build();

        order = orderRepositories.save(order);

        courierKafkaService.sendSearchMessage(CourierSearchMessage.builder()
                .orderId(order.getId())
                .address(address)
                .restaurantId(restaurant.getId())
                .restaurantAddress(restaurantAddress)
                .build());
        
        return orderMapper.toDto(order);
    }

    private void validateOrder(CreateOrderDto createOrderDto) {
        validator.validateObject(createOrderDto);

        if (createOrderDto.getProducts().isEmpty()) {
            throw new NullPointerException("products not set");
        }

        if (!addressProcessing.isValid(createOrderDto.getAddress(), createOrderDto.getCity())) {
            throw new AddressNotInDeliveryAreaException();
        }


    }

    public OrderDto setStatus(long id, OrderStatus status) {

        if (status == null) {
            throw new NullPointerException("status not set");
        }

        if (status == OrderStatus.DELIVERED) {
            return completeOrder(id);
        }

        Optional<Order> orderOptional = orderRepositories.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        Order order = orderOptional.get();
        order.setOrderStatus(status);
        return orderMapper.toDto(orderRepositories.save(order));
    }

    public OrderDto completeOrder(long id) {
        Optional<Order> orderOptional = orderRepositories.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        Order order = orderOptional.get();

        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setTimeEnd(new Time(new Date().getTime()));

        return orderMapper.toDto(orderRepositories.save(order));
    }

    private static class Storage {

        /**
         * stores data about active orders and assigned couriers
         * param Long - courier id
         * param Order - order
         */
        private final static Map<Long, Order> courierActiveOrder = new LinkedHashMap<>();

        private final static Map<Long, List<Order>> restaurantActiveOrder = new LinkedHashMap<>();

        private final static List<Order> activeOrder = new LinkedList<>();

        public static void addRestaurantActiveOrder(long restaurantId, Order order) {
            if (restaurantActiveOrder.get(restaurantId) == null) {

                List<Order> orderList = new LinkedList<>();
                orderList.add(order);
                restaurantActiveOrder.put(restaurantId, orderList);
            } else {
                restaurantActiveOrder.get(restaurantId).add(order);
            }
        }

        public static void addRestaurantActiveOrderList(long restaurantId, List<Order> order) {
            if (restaurantActiveOrder.get(restaurantId) == null) {
                restaurantActiveOrder.put(restaurantId, order);
            } else {
                restaurantActiveOrder.get(restaurantId).addAll(order);
            }
        }
    }


}
