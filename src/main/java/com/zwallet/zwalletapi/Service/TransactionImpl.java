package com.zwallet.zwalletapi.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.TransactionRepository;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransactionImpl implements TransactionService {

    @Autowired
    private TransactionRepository repo;

    @Autowired
    private AccountRepository accountRepo;

    @Override
    public Optional<TransactionEntity> getByReceiverId(AccountEntity receiver) throws ResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<TransactionEntity> getBySenderId(AccountEntity sender) throws ResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> transactionTransfer(TransactionDto dto) throws ResourceNotFoundException {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        AccountEntity receiver = accountRepo.findById(dto.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver with this id is not exist"));
        // --------- Will Change set as the active user who sends the money -----------
        AccountEntity sender = accountRepo.findById(dto.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender with this id is not exist"));
        // ------------------ Will Change end ---------------
        TransactionEntity newTransaction = new TransactionEntity();
        newTransaction.setTransactionAmount(dto.getTransactionAmount());
        newTransaction.setTransactionNotes(dto.getTransactionNotes());
        newTransaction.setTransactionTimestamp(ts);
        newTransaction.setTransactionType(0);
        newTransaction.setTransactionDetail(1);
        newTransaction.setFromAccountId(sender);
        newTransaction.setToAccountId(receiver);

        // Set up Balance
        receiver.setBalance(receiver.getBalance() + dto.getTransactionAmount());
        sender.setBalance((sender.getBalance() - dto.getTransactionAmount()));
        // Try save

        try {
            newTransaction.setIsSuccess(0);
            repo.save(newTransaction);
            accountRepo.save(sender);
            accountRepo.save(receiver);
            return ResponseEntity.ok().body("Transaction Success");
        } catch (Exception e) {
            newTransaction.setIsSuccess(1);
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

}
