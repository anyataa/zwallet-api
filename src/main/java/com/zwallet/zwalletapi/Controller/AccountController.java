package com.zwallet.zwalletapi.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.zwallet.zwalletapi.Config.Encryptor;
import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Service.AccountImp;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    AccountRepository accountRepo;
    @Autowired
    AccountImp accountService;

    @Autowired
    private Encryptor enc;

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
            @RequestParam(name = "userId") Integer userId, @RequestParam(name = "balance") Double balance) {
        return accountService.putAccount(accountId, userId, balance);

    }

    @PutMapping(value = "/balance")
    public ResponseEntity<?> putAccountBalance(@RequestParam(name = "accountId") Integer accountId,
            @RequestParam(name = "balance") Double balance) throws ResourceNotFoundException {
        return accountService.putAccountBalance(accountId, balance);

    }

    @GetMapping(value = "/balance/{accountId}")
    public ResponseEntity<?> getAccountBalance(@PathVariable String accountId) throws ResourceNotFoundException {
        Integer openAccount = enc.decryptString(accountId);
        StatusMessageDto response = new StatusMessageDto<>();
        AccountEntity foundAccount = accountRepo.findById(openAccount)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found account with id " + accountId));
        try {
            response.setMessage("Success");
            response.setStatus(HttpStatus.OK.toString());
            response.setData(foundAccount.getBalance());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setMessage("Failed");
            response.setData(e);
            response.setStatus(HttpStatus.BAD_GATEWAY.toString());
            return ResponseEntity.ok().body(response);
        }

    }

    @GetMapping("username/{username}")
    public AccountEntity getAccountByUserName(@PathVariable(value = "username") String userName)
            throws ResourceNotFoundException {
        AccountEntity foundAccount = accountRepo.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Account with username BCA cannot be found"));
        return foundAccount;
    }

}
