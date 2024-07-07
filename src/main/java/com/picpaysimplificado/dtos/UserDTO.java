package com.picpaysimplificado.dtos;

import com.picpaysimplificado.entities.User;
import com.picpaysimplificado.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UserDTO(
        Long id,
        @NotBlank(message = "Campo obrigatório")
        String firstName,
        @NotBlank(message = "Campo obrigatório")
        String lastName,
        String document,
        BigDecimal balance,
        @Email(message = "Informe um email válido.")
        String email,
        String password,
        UserType userType,

        String statusNotificationService
) {


    public UserDTO(User userEntity) {
        this(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getDocument(),
                userEntity.getBalance(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getUserType(),
                userEntity.getStatusNotificationService()
        );
    }

}
