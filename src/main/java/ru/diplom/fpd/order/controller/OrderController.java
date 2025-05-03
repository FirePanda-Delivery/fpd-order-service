package ru.diplom.fpd.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.diplom.fpd.order.dto.CreateOrderDto;
import ru.diplom.fpd.order.dto.OrderDto;
import ru.diplom.fpd.order.exception.AddressNotInDeliveryAreaException;
import ru.diplom.fpd.order.model.OrderStatus;
import ru.diplom.fpd.order.processing.AddressProcessing;
import ru.diplom.fpd.order.service.OrderServices;


@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final OrderServices orderServices;
    private final AddressProcessing validateAddress;


    @Operation(summary = "Получить цену доставки", deprecated = true)
    @GetMapping("/delivery/cost")
    public ResponseEntity<Map<String, Double>> getCostDelivery() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("cost", 400D);
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Получить данные заказа", parameters = {
            @Parameter(name = "id", description = "Идетификатор заказа", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getOrder(id));
    }

    @Operation(summary = "Получить заказы пользователя", parameters = {
            @Parameter(name = "id", description = "Идетификатор пользователя", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getUserOrders(id));
    }
//
//    @Operation(summary = "Получить заказы курьера", parameters = {
//            @Parameter(name = "id", description = "Идетификатор курьера", in = ParameterIn.PATH, required = true)
//    })
//    @GetMapping("/courier/{id}")
//    public ResponseEntity<List<OrderDto>> getCourierOrders(@PathVariable long id) {
//        return ResponseEntity.ok(orderServices.getCourierOrders(id));
//    }

    @Operation(summary = "Получить заказы ресторана", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<OrderDto>> getRestaurantOrders(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getRestaurantOrders(id));
    }

    @Operation(summary = "Получить активные заказы ресторана", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/restaurant/{id}/active")
    public ResponseEntity<List<OrderDto>> getActiveOrder(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getActiveRestaurantOrder(id));
    }

    @Operation(summary = "Создать заказ")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderDto createOrder)
            throws AddressNotInDeliveryAreaException {

        return ResponseEntity.ok(orderServices.createOrder(createOrder));
    }

    @Operation(summary = "Изменить статус заказа", parameters = {
            @Parameter(name = "id", description = "Идетификатор заказа", in = ParameterIn.PATH, required = true)
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable long id, @RequestBody OrderStatus status) {
        return ResponseEntity.ok(orderServices.setStatus(id, status));
    }
}
