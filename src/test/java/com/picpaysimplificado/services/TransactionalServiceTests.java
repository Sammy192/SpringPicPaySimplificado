package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.entities.User;
import com.picpaysimplificado.enums.UserType;
import com.picpaysimplificado.repositories.TransactionalRepository;
import com.picpaysimplificado.repositories.UserRepository;
import com.picpaysimplificado.services.exceptions.CustomTransactionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class TransactionalServiceTests {

    @Mock
    private UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    private TransactionalRepository transactionalRepository;

    @Mock
    private AuthorizationService authService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionalService transactionService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void createTransactionShouldReturnDoneTransactionDTOWhenTransactionIsAuthorized() throws Exception {
        User sender = new User(null,"Mateus", "Silva","1234999222","mateus@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, new ArrayList<>(), new ArrayList<>(), null);
        User receiver = new User(null,"Jorge", "Silva","1234111222","jorge@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, new ArrayList<>(), new ArrayList<>(), null);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        Mockito.when(authService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(1L, 2L, new BigDecimal(10));
        transactionService.createTransaction(request);

        verify(transactionalRepository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userRepository, times(2)).save(any());



        verify(notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso");
        verify(notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso");
    }

    @Test
    void createTransactionShouldReturnForbiddenWhenTransactionIsNotAuthorized() throws Exception {
        User sender = new User(null,"Mateus", "Silva","1234999222","mateus@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);
        User receiver = new User(null,"Jorge", "Silva","1234111222","jorge@example.com","123456", new BigDecimal("100.00"), UserType.COMMON, null, null, null);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        when(authService.authorizeTransaction(any(), any())).thenReturn(false);

        Exception thrown = Assertions.assertThrows(CustomTransactionException.class, () -> {
            TransactionDTO request = new TransactionDTO(1L, 2L, new BigDecimal(10));
            transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada", thrown.getMessage());
    }


}
