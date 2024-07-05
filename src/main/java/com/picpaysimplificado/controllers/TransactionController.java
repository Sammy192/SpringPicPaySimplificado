package com.picpaysimplificado.controllers;

import com.picpaysimplificado.dtos.DoneTransactionDTO;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.services.TransactionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController()
@RequestMapping(value = "/transactions")
public class TransactionController {

    @Autowired
    private TransactionalService transactionalService;

    @PostMapping
    public ResponseEntity<DoneTransactionDTO> createTransaction(@RequestBody TransactionDTO transaction) throws Exception {
        DoneTransactionDTO newTransaction = transactionalService.createTransaction(transaction);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newTransaction.id()).toUri();

        //return new ResponseEntity<>(newTransaction, HttpStatus.OK);
        return ResponseEntity.created(uri).body(newTransaction);
    }
}
