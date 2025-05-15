package ru.diplom.fpd.order.feign;


import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.diplom.fpd.order.configuration.FeignConfig;
import ru.diplom.fpd.order.dto.CitiesCoordinatesDto;
import ru.diplom.fpd.order.dto.RestaurantDto;

@FeignClient(value = "cityFeignClient", url = "${app.dictionary.url}/city",
        configuration = FeignConfig.class)
public interface CityApi {

    @GetMapping("/coordinates")
    ResponseEntity<List<CitiesCoordinatesDto>> getCoordinates(@RequestParam(name = "city") String city);

}
