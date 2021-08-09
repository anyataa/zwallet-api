package com.zwallet.zwalletapi.Controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.zwallet.zwalletapi.Model.Dto.IncomeOutcomeDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionItemDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodFilterDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Repository.TransactionRepository;
import com.zwallet.zwalletapi.Service.TransactionImpl;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionRepository repo;
    @Autowired
    private TransactionImpl service;

    @Autowired
    private BasicTextEncryptor textEncryptor;

    @Autowired
    private BasicIntegerNumberEncryptor numEncryptor;

    @GetMapping
    public List<TransactionEntity> getAllTransaction() {
        return repo.findAll();
    }

    // transfer activity (One user to another user)
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
    public TransactionPeriodFilterDto getByTime(@PathVariable("accountId") Integer accountId)
            throws ResourceNotFoundException {
        return service.getTransactionPeriodically(accountId);
    }

    @GetMapping("/graph/{accountId}")
    public ResponseEntity<?> getForGraph(@PathVariable("accountId") Integer accountId)
            throws ResourceNotFoundException {
        return service.findTransactionPerDay(accountId);
    }

    @GetMapping("/encrypt")
    public ResponseEntity<?> tryEncrypt() {
        TransactionEntity tra = repo.findById(13).orElse(null);
        // FIRST : We want to encrypt id
        Integer note = tra.getFromAccountId().getAccountId();
        // YET: enrypto only accept BigInteger
        // So we convert it to Big Integer
        // This is how we convert integer to Big int
        BigInteger bi = BigInteger.valueOf(note.intValue());
        // Cons: It was big int, it takes a lot of memories
        BigInteger noteEnc = numEncryptor.encrypt(bi);
        // After the decrypt it will be a big int so convert back to Big Int
        Integer backToInt = Integer.valueOf(bi.intValue());
        // This is how to assign the decrypted value to an Integer field
        // Why Convert? We want Int but it return Big Int
        Integer dec = numEncryptor.decrypt(noteEnc).intValue();
        return ResponseEntity.ok().body(note + "Encryp:" + noteEnc + "Decrypt:" + numEncryptor.decrypt(noteEnc) + dec);
    }

}
