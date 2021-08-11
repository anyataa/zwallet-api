package com.zwallet.zwalletapi.Controller;

import java.net.http.HttpResponse.ResponseInfo;

import com.zwallet.zwalletapi.Config.Encryptor;
import com.zwallet.zwalletapi.Model.Dto.AccountDataFilter;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionReturnDto;
import com.zwallet.zwalletapi.Model.Dto.UserDataFilter;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Service.AccountService;
import com.zwallet.zwalletapi.Service.TransactionService;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.apache.tomcat.util.net.openssl.ciphers.Encryption;
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

    @Autowired
    private Encryptor enc;

    @PostMapping(value = "/topup")
    public ResponseEntity<?> topUpBalance(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        AccountEntity zwallet = accountService.getAccountByUserName("Zwallet");
        AccountEntity receiver = accountService.getAccountById(dto.getOpenToAccountId());
        return transactionService.vendorTransfer(1, 4, dto, receiver, zwallet);
    }

    @PostMapping(value = "/retrieve")
    public ResponseEntity<?> retrieveBalance(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        AccountEntity zwallet = accountService.getAccountByUserName("Zwallet");
        Integer openId = enc.decryptString(dto.getToAccountId());
        AccountEntity sender = accountService.getAccountById(openId);
        return transactionService.vendorTransfer(0, 5, dto, zwallet, sender);
    }

    @PostMapping(value = "/retrieve/bank")
    public ResponseEntity<?> retrieveBalanceByBank(@RequestBody TransactionDto dto) throws ResourceNotFoundException {
        StatusMessageDto response = new StatusMessageDto<>();
        UserDetailEntity bankUser = userRepo.findBankByName(dto.getUsername());
        AccountEntity bank = accountRepo.findByUserId(bankUser);
        Integer openId = enc.decryptString(dto.getToAccountId());
        AccountEntity sender = accountService.getAccountById(openId);

        // filter.setsenBalance(dto.getTransactionAmount());
        // filter.setSender(sender.getUserId().getUsername());
        // filter.setReceiver(bank.getUserId().getUsername());
        try {

            // response.setData(filter);
            // response.setMessage("Success");
            // response.setStatus(HttpStatus.ACCEPTED.toString());
            return transactionService.vendorTransfer(0, 5, dto, bank, sender);
        } catch (Exception e) {
            response.setData(null);
            response.setMessage("Error");
            response.setStatus(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.ok().body(response);
        }

    }

}
