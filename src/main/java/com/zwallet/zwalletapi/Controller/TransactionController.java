package com.zwallet.zwalletapi.Controller;

import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Repository.TransactionRepository;
import com.zwallet.zwalletapi.Service.TransactionImpl;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository repo;
    @Autowired
    private TransactionImpl service;

    @GetMapping
    public List<TransactionEntity> getAllTransaction() {
        return repo.findAll();
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> postTransaction(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        return service.transactionTransfer(dto);
    }

    @GetMapping("/transfer")
    public List<TransactionEntity> getTransaction() {
        return repo.findAll();
    }

}
