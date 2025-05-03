package ru.diplom.fpd.order.processing;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import ru.diplom.fpd.order.dto.Coordinates;

@RequiredArgsConstructor
public class MoneyProcessing {

    @Value("${delivery.cost.coefficient}")
    private double coefficient;

    private final AddressProcessing addressProcessing;


    @Value("${yandex.api.key}")
    String apiKey;


    public double getCostDelivery(String restAddress, String address) {

        Coordinates addressCords = addressProcessing.getCords(address);
        Coordinates coordinates = addressProcessing.getCords(restAddress);

        return Math.sqrt(Math.pow((addressCords.getX() - coordinates.getX()), 2) +
                Math.pow((addressCords.getY() - coordinates.getY()), 2)) *
                coefficient;
    }



}
