package com.zwallet.zwalletapi.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepo;

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

}
