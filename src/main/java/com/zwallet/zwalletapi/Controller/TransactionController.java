package com.zwallet.zwalletapi.Controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.zwallet.zwalletapi.Config.Encryptor;
import com.zwallet.zwalletapi.Model.Dto.IncomeOutcomeDto;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionItemDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodFilterDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.TransactionRepository;
import com.zwallet.zwalletapi.Service.TransactionImpl;
import com.zwallet.zwalletapi.Service.TransactionService;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    TransactionService transactionService;

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    private Encryptor enc;

    @Autowired
    public static BasicTextEncryptor textEncryptor;

    @Autowired
    private BasicIntegerNumberEncryptor numEncryptor;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public IncomeOutcomeDto getByReceiverId(@PathVariable("accountId") String encAccountId)
            throws ResourceNotFoundException {
        Integer accountId = enc.decryptString(encAccountId);
        return service.getAllTransaction(accountId);
    }

    @GetMapping("/history/{accountId}")
    public TransactionPeriodFilterDto getByTime(@PathVariable("accountId") String encAccountId)
            throws ResourceNotFoundException {
        Integer accountId = enc.decryptString(encAccountId);
        return service.getTransactionPeriodically(accountId);
    }

    @GetMapping("/graph/{accountId}")
    public ResponseEntity<?> getForGraph(@PathVariable("accountId") String encAccountId)
            throws ResourceNotFoundException {
        Integer accountId = enc.decryptString(encAccountId);
        return service.findTransactionPerDay(accountId);
    }
    // 1 "Transfer",
    // 2 "Subscription",
    // 3 "Payment",
    // 4 "Top Up",
    // 5 "Retrieve",

    // Transaction Payments
    @PostMapping("/payments/{username}")
    public ResponseEntity<?> postPayment(@PathVariable("username") String userName, @RequestBody TransactionDto dto)
            throws ResourceNotFoundException {
        StatusMessageDto res = new StatusMessageDto<>();
        AccountEntity merchant = accountRepo.findByUsername(userName).get();
        Integer openId = enc.decryptString(dto.getToAccountId());
        AccountEntity sender = accountRepo.findById(openId).get();

        try {
            return transactionService.vendorTransfer(0, 3, dto, merchant, sender);
            // return ResponseEntity.ok().body(sender);
        } catch (Exception e) {
            res.setMessage("Error");
            res.setData(e);
            return ResponseEntity.ok().body(e);

        }

    }

    // Try Encrypt
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
        // BigInteger noteEnc = numEncryptor.encrypt(bi);
        // After the decrypt it will be a big int so convert back to Big Int
        // Integer backToInt = Integer.valueOf(bi.intValue());
        // This is how to assign the decrypted value to an Integer field
        // Why Convert? We want Int but it return Big Int
        // Integer dec = numEncryptor.decrypt(noteEnc).intValue();
        Integer userId = tra.getToAccountId().getAccountId();
        String enc = encryptInt(userId);

        return ResponseEntity.ok()
                .body("enc : " + enc + "dec: " + decryptString(enc) + " try int:" + (decryptString(enc) + 2));
    }

    public static String encryptInt(Integer id) {
        String encData = textEncryptor.encrypt(id.toString());
        String encode = Base64.getEncoder().encodeToString(encData.getBytes());
        return encode;
    };

    public static Integer decryptString(String encodedId) {
        String decodedId = new String(Base64.getDecoder().decode(encodedId));
        Integer decData = Integer.parseInt(textEncryptor.decrypt(decodedId));
        return decData;

    }

}
