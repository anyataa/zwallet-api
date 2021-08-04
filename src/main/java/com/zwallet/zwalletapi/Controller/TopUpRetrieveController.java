package com.zwallet.zwalletapi.Controller;

import java.net.http.HttpResponse.ResponseInfo;

import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Service.AccountService;
import com.zwallet.zwalletapi.Service.TransactionService;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zwallet")
@CrossOrigin(origins = "*")
public class TopUpRetrieveController {

    @Autowired
    AccountService accountService;

    @Autowired
    UserDetailRepository userRepo;

    @Autowired
    AccountRepository accountRepo;

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

    @PostMapping(value = "/retrieve/bank")
    public ResponseEntity<?> retrieveBalanceByBank(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        StatusMessageDto<AccountEntity> response = new StatusMessageDto<>();
        UserDetailEntity bankUser = userRepo.findBankByName(dto.getUsername());
        AccountEntity bank = accountRepo.findByUserId(bankUser);
        AccountEntity sender = accountService.getAccountById(dto.getToAccountId());
        try {
            transactionService.vendorTransfer(0, 5, dto, bank, sender);
            response.setData(sender);
            response.setMessage("Success");
            response.setStatus(HttpStatus.ACCEPTED.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setData(null);
            response.setMessage("Error");
            response.setStatus(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.ok().body(response);
        }

    }

}
