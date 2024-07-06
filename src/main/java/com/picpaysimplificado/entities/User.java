package com.picpaysimplificado.entities;

import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;
    private String password;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receivedTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions = new ArrayList<>();

    public User(UserDTO userDTO) {
        this.firstName = userDTO.firstName();
        this.lastName = userDTO.lastName();
        this.document = userDTO.document();
        this.email = userDTO.email();
        this.password = userDTO.password();
        this.balance = userDTO.balance();
        this.userType = userDTO.userType();
    }

    public void addSentTransaction(Transaction transaction) {
        sentTransactions.add(transaction);
        transaction.setSender(this);
    }

    public void removeSentTransaction(Transaction transaction) {
        sentTransactions.remove(transaction);
        transaction.setSender(null);
    }

    public void addReceivedTransaction(Transaction transaction) {
        receivedTransactions.add(transaction);
        transaction.setReceiver(this);
    }

    public void removeReceivedTransaction(Transaction transaction) {
        receivedTransactions.remove(transaction);
        transaction.setReceiver(null);
    }

}
