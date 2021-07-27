package com.zwallet.zwalletapi.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.zwallet.zwalletapi.Model.Dto.IncomeOutcomeDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionItemDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodFilterDto;
import com.zwallet.zwalletapi.Model.Dto.VendorDto;
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
    public IncomeOutcomeDto getAllTransaction(Integer accountId) throws ResourceNotFoundException {
        AccountEntity foundAccount = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find account with id : " + accountId));

        // Receiver : Income
        List<TransactionEntity> foundIncome = repo.findByToAccountId(foundAccount);
        Double sumIncome = 0D;
        for (TransactionEntity item : foundIncome) {
            sumIncome = sumIncome + item.getTransactionAmount();
        }

        // Sender : Outcome
        List<TransactionEntity> foundOutcome = repo.findByFromAccountId(foundAccount);
        Double sumOutcome = 0D;
        for (TransactionEntity item : foundOutcome) {
            sumOutcome += item.getTransactionAmount();
        }

        // All Transaction
        List<TransactionEntity> foundTransaction = repo.findAllTransactionByAccountId(foundAccount);

        IncomeOutcomeDto data = new IncomeOutcomeDto(sumIncome, sumOutcome, foundIncome, foundOutcome,
                foundTransaction);
        return data;
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
        // Check balance
        if (sender.getBalance() <= dto.getTransactionAmount()) {
            return ResponseEntity.ok().body("Amount Exceeds Balance");
        }

        // Set up Balance
        receiver.setBalance(receiver.getBalance() + dto.getTransactionAmount());
        sender.setBalance((sender.getBalance() - dto.getTransactionAmount()));
        // Try save

        try {
            newTransaction.setIsSuccess(0);
            repo.save(newTransaction);
            accountRepo.save(sender);
            accountRepo.save(receiver);
            return ResponseEntity.ok().body(newTransaction);
        } catch (Exception e) {
            newTransaction.setIsSuccess(1);
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @Override
    public TransactionPeriodFilterDto getTransactionPeriodically(Integer accountId) throws ResourceNotFoundException {
        AccountEntity foundAccount = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("cannot find this account id"));

        TransactionPeriodDto history = new TransactionPeriodDto();
        TransactionPeriodFilterDto filterPeriod = new TransactionPeriodFilterDto();
        history.setWeekly(repo.findAllTransactionLastWeek(foundAccount));
        List<TransactionItemDto> filterWeekly = new ArrayList<>();
        for (TransactionEntity item : history.getWeekly()) {
            TransactionItemDto temp = new TransactionItemDto();
            temp.setAmount(item.getTransactionAmount());
            temp.setSender(item.getFromAccountId().getUserId().getUsername());
            temp.setReceiver(item.getToAccountId().getUserId().getUsername());
            temp.setDate(item.getTransactionTimestamp());
            temp.setTransactionDetails(item.getTransactionDetail());
            temp.setTransactionType(item.getTransactionType());
            temp.setTransactionNotes(item.getTransactionNotes());
            filterWeekly.add(temp);
        }
        filterPeriod.setWeekly(filterWeekly);
        return filterPeriod;

    }

    @Override
    public ResponseEntity<?> vendorTransfer(Integer transactionType, Integer transactionDetail, TransactionDto dto,
            AccountEntity receiver, AccountEntity sender) throws ResourceNotFoundException {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        // ------------------ Will Change end ---------------
        TransactionEntity newTransaction = new TransactionEntity();
        newTransaction.setTransactionAmount(dto.getTransactionAmount());
        newTransaction.setTransactionNotes(dto.getTransactionNotes());
        newTransaction.setTransactionTimestamp(ts);
        newTransaction.setTransactionType(transactionType);
        newTransaction.setTransactionDetail(transactionDetail);
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
            return ResponseEntity.ok().body(newTransaction);
        } catch (Exception e) {
            newTransaction.setIsSuccess(1);
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

}
