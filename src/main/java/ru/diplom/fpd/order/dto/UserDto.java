package ru.diplom.fpd.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link ru.diplom.fpd.model.User}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {
    private final long id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String email;
}