package com.picpaysimplificado.dtos;

import com.picpaysimplificado.entities.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record DoneTransactionDTO(
        Long id,
        BigDecimal amount,
        UserDTO sender,
        UserDTO receiver,
        Instant timestamp
) {

    public DoneTransactionDTO(Transaction newTransaction) {
        this(
                newTransaction.getId(),
                newTransaction.getAmount(),
                new UserDTO(newTransaction.getSender()),
                new UserDTO(newTransaction.getReceiver()),
                newTransaction.getTimestamp()
        );
    }
}
