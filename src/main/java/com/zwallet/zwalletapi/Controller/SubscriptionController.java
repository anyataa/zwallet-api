package com.zwallet.zwalletapi.Controller;

import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Service.AccountImp;
import com.zwallet.zwalletapi.Service.AccountService;
import com.zwallet.zwalletapi.Service.TransactionImpl;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/merchant")
public class SubscriptionController {
    @Autowired
    TransactionImpl transactionService;

    @Autowired
    AccountImp accountService;

    @PostMapping
    public ResponseEntity<?> postSubscription(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        // AccountEntity subscribeTo =
        // accountService.getAccountByUserName(dto.getUsername());

        // AccountEntity userAccount =
        // accountService.getAccountById(dto.getFromAccountId());
        // transactionService.vendorTransfer(0, 2, dto, subscribeTo, userAccount);
        return ResponseEntity.ok().body("transaction success");
    }

}
