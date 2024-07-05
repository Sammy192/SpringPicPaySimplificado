package com.picpaysimplificado.dtos;

import com.picpaysimplificado.entities.User;
import com.picpaysimplificado.enums.UserType;

import java.math.BigDecimal;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String document,
        BigDecimal balance,
        String email,
        String password,
        UserType userType
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
                userEntity.getUserType()
        );
    }

}
