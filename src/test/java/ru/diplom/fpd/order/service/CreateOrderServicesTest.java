package ru.diplom.fpd.order.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.validation.Validator;
import ru.diplom.fpd.order.dto.CreateOrderDto;
import ru.diplom.fpd.order.dto.OrderDto;
import ru.diplom.fpd.order.dto.OrderProductDto;
import ru.diplom.fpd.order.dto.ProductDto;
import ru.diplom.fpd.order.dto.RestaurantAddressDto;
import ru.diplom.fpd.order.dto.RestaurantDto;
import ru.diplom.fpd.order.dto.kafka.CourierSearchMessage;
import ru.diplom.fpd.order.feign.RestaurantApi;
import ru.diplom.fpd.order.mapper.OrderMapper;
import ru.diplom.fpd.order.mapper.ProductMapper;
import ru.diplom.fpd.order.model.Order;
import ru.diplom.fpd.order.model.OrderProduct;
import ru.diplom.fpd.order.model.OrderStatus;
import ru.diplom.fpd.order.processing.AddressProcessing;
import ru.diplom.fpd.order.repository.OrderRepositories;


class CreateOrderServicesTest {

    @Mock
    private OrderRepositories orderRepositories;
    @Mock
    private CourierKafkaService courierKafkaService;
    @Mock
    private RestaurantApi restaurantApi;
    @Mock
    private AddressProcessing addressProcessing;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private Validator validator;

    @InjectMocks
    OrderServices orderServices;

    @Test
    @ExtendWith(MockitoExtension.class)
    void shouldCreateOrderAndSendSearchMessageToKafka() {

        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setProducts(Set.of(new OrderProductDto(1, 1, new ProductDto(1))));
        createOrderDto.setRestaurantId(1L);
        createOrderDto.setCity("TestCity");
        createOrderDto.setAddress("Test Address");
        createOrderDto.setUserId(123L);

        RestaurantAddressDto restaurantAddressDto = new RestaurantAddressDto("Restaurant Address", "Воронеж");

        OrderProduct orderProduct = new OrderProduct();

        RestaurantDto restaurantDto = new RestaurantDto(1L , "test", "tet", null,
                null, 100.0, 5, false, null, List.of(restaurantAddressDto));

        Order savedOrder = new Order();
        savedOrder.setId(1L);

        OrderDto expectedOrderDto = new OrderDto(1L, 1L, null, null, OrderStatus.DELIVERY,
                1L, null, null, "Addres", "Воронеж", restaurantAddressDto);

        when(addressProcessing.isValid(createOrderDto.getAddress(), createOrderDto.getCity())).thenReturn(true);
        when(productMapper.toOrderEntity(any())).thenReturn(orderProduct);
        when(restaurantApi.getRestaurant(createOrderDto.getRestaurantId())).thenReturn(ResponseEntity.ok(restaurantDto));
        when(addressProcessing.restaurantNearestToAddress(restaurantDto, createOrderDto.getCity(), createOrderDto.getAddress()))
                .thenReturn(restaurantAddressDto);
        when(orderRepositories.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toDto(savedOrder)).thenReturn(expectedOrderDto);

        OrderDto result = orderServices.createOrder(createOrderDto);

        assertNotNull(result);
        assertEquals(expectedOrderDto, result);

        verify(validator).validateObject(createOrderDto);
        verify(addressProcessing).isValid(createOrderDto.getAddress(), createOrderDto.getCity());
        verify(productMapper, times(createOrderDto.getProducts().size())).toOrderEntity(any());
        verify(restaurantApi).getRestaurant(createOrderDto.getRestaurantId());
        verify(addressProcessing).restaurantNearestToAddress(restaurantDto, createOrderDto.getCity(),
                createOrderDto.getAddress());
        verify(orderRepositories).save(any(Order.class));
        verify(courierKafkaService).sendSearchMessage(any(CourierSearchMessage.class));
        verify(orderMapper).toDto(savedOrder);

    }
}