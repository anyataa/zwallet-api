package com.zwallet.zwalletapi.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Service.AccountImp;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepo;
    @Autowired
    AccountImp accountService;

    @GetMapping
    public List<AccountEntity> getAllAccount() {
        return accountRepo.findAll();
    }

    @GetMapping("/{accountId}")
    public AccountEntity getAccountById(@PathVariable(name = "accountId") Integer accountId)
            throws ResourceNotFoundException {
        AccountEntity foundAccount = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("cannot find account with id : " + accountId));
        return foundAccount;
    }

    @GetMapping("user/{user_id}")
    public AccountEntity getAccountUserByUserId(@PathVariable(name = "user_id") Integer userId)
            throws ResourceNotFoundException {
        AccountEntity foundAccount = accountRepo.getAccountUserByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("cannot find account with user id : " + userId));
        return foundAccount;

    }

    @PostMapping
    public ResponseEntity<?> postAccount(@RequestBody AccountDto dto) {
        return accountService.postAccount(dto);
    }

    @PutMapping
    public ResponseEntity<?> putAccount(@RequestParam(name = "accountId") Integer accountId,
            @RequestParam(name = "userId") Integer userId) {
        return accountService.putAccount(accountId, userId);

    }

    @GetMapping("/bca")
    public AccountEntity getAccountByUserName() {
        AccountEntity foundAccount = accountRepo.findByUsername("BCA");
        return foundAccount;
    }

}
