package com.zwallet.zwalletapi.Controller;

import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Service.AccountService;
import com.zwallet.zwalletapi.Service.TransactionService;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zwallet")
public class TopUpRetrieveController {

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/topup")
    public ResponseEntity<?> topUpBalance(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        AccountEntity zwallet = accountService.getAccountByUserName("Zwallet");
        AccountEntity receiver = accountService.getAccountById(dto.getToAccountId());
        return transactionService.vendorTransfer(1, 4, dto, receiver, zwallet);
    }

    @PostMapping(value = "/retrieve")
    public ResponseEntity<?> retrieveBalance(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        AccountEntity zwallet = accountService.getAccountByUserName("Zwallet");
        AccountEntity sender = accountService.getAccountById(dto.getToAccountId());
        return transactionService.vendorTransfer(0, 5, dto, zwallet, sender);
    }

}
