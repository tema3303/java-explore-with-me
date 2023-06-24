package ru.practicum.ewm.users.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;
    private long id;
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}