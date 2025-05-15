package ru.diplom.fpd.order.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.diplom.fpd.order.configuration.property.CourierKafkaProperties;
import ru.diplom.fpd.order.configuration.property.DictionaryKafkaProperties;
import ru.diplom.fpd.order.feign.CityApi;
import ru.diplom.fpd.order.feign.RestaurantApi;
import ru.diplom.fpd.order.feign.UserApi;


@Configuration
@EnableFeignClients(clients = {RestaurantApi.class, CityApi.class, UserApi.class})
@EnableConfigurationProperties({CourierKafkaProperties.class, DictionaryKafkaProperties.class})
public class ApplicationConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean(name = "yandexMapsRestTemplate")
    RestTemplate createYMapsRestTemplate() {
        return new RestTemplateBuilder()
                .build();

    }

}