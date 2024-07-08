package com.picpaysimplificado.repositories;

import com.picpaysimplificado.entities.Transaction;
import com.picpaysimplificado.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionalRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderOrReceiver(User sender, User receiver);

    List<Transaction> findBySender_DocumentOrReceiver_Document(String senderDocument, String receiverDocument);
}
