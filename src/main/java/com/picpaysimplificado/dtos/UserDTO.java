package com.picpaysimplificado.dtos;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;

import java.math.BigDecimal;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String document;
    private BigDecimal balance;
    private String email;
    private String password;
    private UserType userType;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {
        this.id= id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.document = document;
        this.balance = balance;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public UserDTO(User userEntity) {
        this.id= userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.document = userEntity.getDocument();
        this.balance = userEntity.getBalance();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.userType = userEntity.getUserType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
