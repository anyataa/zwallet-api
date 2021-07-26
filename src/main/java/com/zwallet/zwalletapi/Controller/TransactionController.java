package com.zwallet.zwalletapi.Controller;

import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.IncomeOutcomeDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Repository.TransactionRepository;
import com.zwallet.zwalletapi.Service.TransactionImpl;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{accountId}")
    public IncomeOutcomeDto getByReceiverId(@PathVariable("accountId") Integer accountId)
            throws ResourceNotFoundException {
        return service.getAllTransaction(accountId);
    }

    @GetMapping("/history/{accountId}")
    public TransactionPeriodDto getByTime(@PathVariable("accountId") Integer accountId)
            throws ResourceNotFoundException {
        return service.getTransactionPeriodically(accountId);
    }

}
