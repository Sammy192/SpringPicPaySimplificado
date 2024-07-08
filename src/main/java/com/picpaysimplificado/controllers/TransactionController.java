package com.picpaysimplificado.controllers;

import com.picpaysimplificado.dtos.DoneTransactionDTO;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.services.TransactionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping(value = "/transactions")
public class TransactionController {

    @Autowired
    private TransactionalService transactionalService;

    @PostMapping
    public ResponseEntity<DoneTransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transaction) throws Exception {
        DoneTransactionDTO newTransaction = transactionalService.createTransaction(transaction);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newTransaction.id()).toUri();

        //return new ResponseEntity<>(newTransaction, HttpStatus.OK);
        return ResponseEntity.created(uri).body(newTransaction);
    }

    @GetMapping
    public ResponseEntity<List<DoneTransactionDTO>> getAllTransactions() {
        List<DoneTransactionDTO> transactions =  transactionalService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DoneTransactionDTO>> getTransactionsByUserId(@PathVariable Long userId) {
        List<DoneTransactionDTO> transactions = transactionalService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/user/document/{userDocument}")
    public ResponseEntity<List<DoneTransactionDTO>> getTransactionsByUserDocument(@PathVariable String userDocument) {
        List<DoneTransactionDTO> transactions = transactionalService.getTransactionsByUserDocument(userDocument);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
