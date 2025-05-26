package ru.diplom.fpd.order.processing;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.diplom.fpd.order.dto.CitiesCoordinatesDto;
import ru.diplom.fpd.order.dto.Coordinates;
import ru.diplom.fpd.order.dto.RestaurantAddressDto;
import ru.diplom.fpd.order.dto.RestaurantDto;
import ru.diplom.fpd.order.dto.yandex.GeoObject;
import ru.diplom.fpd.order.dto.yandex.GeoObjectCollection;
import ru.diplom.fpd.order.exception.AddressNotInDeliveryAreaException;
import ru.diplom.fpd.order.feign.CityApi;

@Component
@RequiredArgsConstructor()
public class AddressProcessing {

    @Qualifier("yandexMapsRestTemplate")
    private final RestTemplate restTemplate;
    private final CityApi cityApi;

    @Value("${yandex.api.key}")
    String apiKey;

    public boolean isValid(String address, String city) {

        return isValid(address, cityApi.getCoordinates(city).getBody());
    }


    public boolean isValid(String address, List<CitiesCoordinatesDto> cityCoordinates) {

        Coordinates cords = getCords(address);
        List<Coordinates> list = cityCoordinates.stream()
                .sorted(Comparator.comparing(CitiesCoordinatesDto::getIndex))
                .collect(Collectors.toList());

        return horizontalTracing(list, cords);
    }

    public RestaurantAddressDto restaurantNearestToAddress(RestaurantDto restaurant, String city, String address) {

        if (restaurant == null) {
            throw new NullPointerException("restaurant is null");
        }

        if (address == null || address.isEmpty()) {
            throw new NullPointerException("address is null or empty");
        }

        Coordinates addressCoordinates = getCords(address);

        return restaurant.getAddresses().stream()
                .filter(restaurantAddress -> restaurantAddress.getCity().equalsIgnoreCase(city))
                .map(restaurantAddress -> {
                    Coordinates coordinates = getCords(restaurantAddress.getAddress());
                    return Pair.of(Math.sqrt(Math.pow((addressCoordinates.getX() - coordinates.getX()), 2) +
                                    Math.pow((addressCoordinates.getY() - coordinates.getY()), 2)),
                            restaurantAddress);
                })
                .min(Comparator.comparing(Pair::getFirst))
                .map(Pair::getSecond)
                .orElseThrow(() ->
                        new AddressNotInDeliveryAreaException("The restaurant " + restaurant.getName()
                                + " does not have delivery in the city of " + city));
    }

    private boolean horizontalTracing(List<Coordinates> cords, Coordinates objectCords) {

        int numberIntersections = 0;

        int size = cords.size();
        double x = objectCords.getX();
        double y = objectCords.getY();

        for (int i = 0, j = 1; i < size; i++, j = i == size - 1 ? 0 : j + 1) {

            double x1 = cords.get(i).getX(); // координаты х начала отрезка
            double y1 = cords.get(i).getY(); // координата y начала отрезка

            double x2 = cords.get(j).getX(); // координаты х конца отрезка
            double y2 = cords.get(j).getY(); // координата y конца отрезка


            if ((x - x1) * (y2 - y1) - (y - y1) * (x2 - x1) == 0) {
                if (((x1 < x2) && (x1 <= x) && (x <= x2))
                        || ((x1 > x2) && (x2 <= x) && (x <= x1))
                        || ((x1 == x2) && (y1 < y2) && (y1 < y) && (y < y2))
                        || (x1 == x2) && (y2 < y1) && (y2 < y) && (y < y1)) {

                    return true;
                }
            }

            if ((y - y1) * (y - y2) < 0) {
                if (x < ((y - y1) * (x2 - x1) / (y2 - y1) + x1)) {
                    numberIntersections++;
                }

            } else if (y1 == y2 && y1 == y) {

                Coordinates lastStartCords; // вершина начала предыдущего отрезка
                Coordinates nextEndCords; // вершина конца следуюшего отрезка

                if (i == 0) {
                    lastStartCords = cords.get(size - 1);
                } else {
                    lastStartCords = cords.get(i - 1);
                }

                if (i == size - 2) {
                    nextEndCords = cords.get(1);
                } else {
                    nextEndCords = cords.get(j + 1);
                }

                if ((y - lastStartCords.getY()) * (y - nextEndCords.getY()) < 0) {
                    numberIntersections++;
                } else {
                    numberIntersections += 2;
                }

            } else if (y == y1 && x1 > x) {

                Coordinates lastCords; // предыдущая вершина

                if (i == 1) {
                    lastCords = cords.get(size - 1);
                } else {
                    lastCords = cords.get(i - 1);
                }

                if ((y - lastCords.getY()) * (y - y2) < 0) {
                    numberIntersections++;
                } else {
                    numberIntersections += 2;
                }
            }
        }

        return numberIntersections % 2 != 0;
    }


    public final Coordinates getCords(String address) {

        if (address == null || address.isEmpty()) {
            throw new NullPointerException("address not set");
        }

        String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://geocode-maps.yandex.ru/1.x")
                .queryParam("apikey", "{apikey}")
                .queryParam("format", "{format}")
                .queryParam("results", "{results}")
                .queryParam("geocode", "{geocode}")
                .encode()
                .toUriString();

        Map<String, Object> params = Map.of(
                "apikey", apiKey,
                "format", "json",
                "results", 1,
                "geocode", address);

        List<GeoObject> result = restTemplate
                .exchange(urlTemplate, HttpMethod.GET, null,
                        GeoObjectCollection.class,
                        params)
                .getBody()
                .getGeoObjects();

        return Optional.ofNullable(result)
                .filter(Predicate.not(List::isEmpty))
                .map(list -> list.get(0))
                .map(GeoObject::getPoint)
                .map(Coordinates::toCoordinates)
                .orElseThrow();
    }

}


