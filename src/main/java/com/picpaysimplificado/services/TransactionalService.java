package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.DoneTransactionDTO;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.entities.Transaction;
import com.picpaysimplificado.entities.User;
import com.picpaysimplificado.repositories.TransactionalRepository;
import com.picpaysimplificado.repositories.UserRepository;
import com.picpaysimplificado.services.exceptions.CustomTransactionException;
import com.picpaysimplificado.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TransactionalService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionalRepository transactionalRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DoneTransactionDTO createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = userRepository.findById(transactionDTO.senderId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Usuário sender id %d não encontrado.", transactionDTO.senderId())));

        User receiver = userRepository.findById(transactionDTO.receiverId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Usuário receiver id %d não encontrado.", transactionDTO.receiverId())));

        userService.validateTransactional(sender, transactionDTO.value());

        boolean isAuthorized = authorizationService.authorizeTransaction(sender, transactionDTO.value());

        if(!isAuthorized) {
            throw new CustomTransactionException("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();

        copyDtoToEntity(transactionDTO, newTransaction, sender, receiver);

        saveNewTransaction(sender, receiver, transactionDTO, newTransaction);

        notificationService.sendNotification(sender, "Transação realizada com sucesso");
        notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return new DoneTransactionDTO(newTransaction);

    }

    private void copyDtoToEntity(TransactionDTO transactionDTO, Transaction transactionalEntity, User userSender, User userReceiver) {
        transactionalEntity.setAmount(transactionDTO.value());
        transactionalEntity.setSender(userSender);
        transactionalEntity.setReceiver(userReceiver);
        transactionalEntity.setTimestamp(Instant.now());
    }

    private void saveNewTransaction(User sender, User receiver, TransactionDTO transactionDTO, Transaction newTransaction) {
        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

        newTransaction = transactionalRepository.save(newTransaction);
        userRepository.save(sender);
        userRepository.save(receiver);
    }
}
