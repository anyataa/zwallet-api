package com.zwallet.zwalletapi.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Service.AccountServiceImpl;
import com.zwallet.zwalletapi.Utils.ExceptionNotFound;

import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    AccountServiceImpl accountService;

    @GetMapping
    public List<AccountEntity> getAllAccount() {
        return accountRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> postAccount(@RequestBody AccountDto dto) {
        AccountEntity newAccount = new AccountEntity();
        dto.setData(dto.getBalance(), newAccount);
        try {
            accountRepo.save(newAccount);
            return ResponseEntity.ok().body(newAccount);
        } catch (Exception e) {
            return ResponseEntity.ok().body(newAccount + " failed to save");
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable(value = "accountId") Integer accountId)
            throws ExceptionNotFound {
        AccountEntity foundAccount = accountService.getAccountById(2)
                .orElseThrow(() -> new ExceptionNotFound("cannot find account with id : " + accountId));
        return ResponseEntity.ok().body(foundAccount);
    }

}
