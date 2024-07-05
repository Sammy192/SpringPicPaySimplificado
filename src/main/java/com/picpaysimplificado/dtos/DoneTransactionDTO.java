package com.picpaysimplificado.dtos;

import com.picpaysimplificado.domain.transaction.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

public class DoneTransactionDTO {

    private Long id;
    private BigDecimal amount;
    private UserDTO sender;
    private UserDTO receiver;
    private Instant timestamp;

    public  DoneTransactionDTO() {
    }

    public DoneTransactionDTO(Long id, BigDecimal amount, UserDTO sender, UserDTO receiver, Instant timestamp) {
        this.id = id;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public DoneTransactionDTO(Transaction newTransaction) {
        this.id = newTransaction.getId();
        this.amount = newTransaction.getAmount();
        this.sender = new UserDTO(newTransaction.getSender());
        this.receiver = new UserDTO(newTransaction.getReceiver());
        this.timestamp = newTransaction.getTimestamp();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(UserDTO receiver) {
        this.receiver = receiver;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DoneTransactionDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", timestamp=" + timestamp +
                '}';
    }
}
