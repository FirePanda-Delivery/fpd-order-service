package ru.diplom.fpd.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {

    private double x;

    private double y;

    public static Coordinates toCoordinates(String str) {
        String[] arr = str.split(" ");
        Coordinates cords = new Coordinates();
        cords.setX(Double.parseDouble(arr[0]));
        cords.setY(Double.parseDouble(arr[1]));

        return cords;
    }
}
