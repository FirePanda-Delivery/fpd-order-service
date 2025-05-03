package ru.diplom.fpd.order.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.diplom.fpd.order.dto.RestaurantDto;

@FeignClient(value = "restaurantFeignClient", url = "${app.dictionary.url}/restaurant")
public interface RestaurantApi {


    @GetMapping("/{id}")
    ResponseEntity<RestaurantDto> getRestaurant(@PathVariable long id);




}
