package com.picpaysimplificado.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotNull(message = "O remetente é obrigatório")
        Long senderId,
        @NotNull(message = "O destinatário é obrigatório")
        Long receiverId,
        @NotNull(message = "Informe um valor.")
        @Positive(message = "Informe um valor maior que zero.")
        BigDecimal value
) {
}