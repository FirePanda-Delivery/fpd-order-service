package ru.diplom.fpd.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.diplom.fpd.order.configuration.FeignConfig;
import ru.diplom.fpd.order.dto.UserDto;

@FeignClient(value = "userFeignClient", url = "${app.dictionary.url}/user", configuration = FeignConfig.class)
public interface UserApi {

    @GetMapping()
    ResponseEntity<UserDto> getUser(@RequestParam String username);
}
