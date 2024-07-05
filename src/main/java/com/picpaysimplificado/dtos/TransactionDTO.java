package com.picpaysimplificado.dtos;

import java.math.BigDecimal;

/*public record TransactionDTO(Long senderId, Long receiverId, BigDecimal value) {
}*/
public class TransactionDTO {

    private Long senderId;
    private Long receiverId;
    private BigDecimal value;

    public TransactionDTO() {
    }

    public TransactionDTO(Long senderId, Long receiverId, BigDecimal value) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.value = value;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}