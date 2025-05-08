package ru.diplom.fpd.order.exception;

public class AddressNotInDeliveryAreaException extends RuntimeException {

    public AddressNotInDeliveryAreaException() {
        super("the address is not in the delivery area");
    }

    public AddressNotInDeliveryAreaException(String message) {
        super(message);
    }
}
