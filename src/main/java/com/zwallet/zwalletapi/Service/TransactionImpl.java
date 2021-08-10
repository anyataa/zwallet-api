package com.zwallet.zwalletapi.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.zwallet.zwalletapi.Config.Encryptor;
import com.zwallet.zwalletapi.Model.Dto.IncomeOutcomeDto;
import com.zwallet.zwalletapi.Model.Dto.StatusMessageDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionBalanceHistoryDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionGraphDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionItemDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodFilterDto;
import com.zwallet.zwalletapi.Model.Dto.VendorDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.TransactionRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransactionImpl implements TransactionService {

    @Autowired
    private TransactionRepository repo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private Encryptor enc;

    @Autowired
    private UserDetailRepository userRepo;

    @Override
    public IncomeOutcomeDto getAllTransaction(Integer accountId) throws ResourceNotFoundException {
        AccountEntity foundAccount = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find account with id : " + accountId));

        // As Receiver : Income
        List<TransactionEntity> foundIncome = repo.findByToAccountId(foundAccount);
        List<TransactionItemDto> foundFilterIncome = new ArrayList<>();
        for (TransactionEntity item : foundIncome) {
            TransactionItemDto filter = new TransactionItemDto(item.getFromAccountId().getUserId().getUsername(),
                    item.getToAccountId().getUserId().getUsername(), item.getTransactionAmount(),
                    item.getTransactionTimestamp(), item.getTransactionType(), item.getTransactionDetail(),
                    item.getTransactionNotes());
            foundFilterIncome.add(filter);
        }
        // Sum Income
        Double sumIncome = 0D;
        for (TransactionEntity item : foundIncome) {
            sumIncome = sumIncome + item.getTransactionAmount();
        }

        // As Sender : Outcome
        List<TransactionEntity> foundOutcome = repo.findByFromAccountId(foundAccount);
        List<TransactionItemDto> foundFilterOutcome = new ArrayList<>();
        for (TransactionEntity item : foundOutcome) {
            TransactionItemDto filter = new TransactionItemDto(item.getFromAccountId().getUserId().getUsername(),
                    item.getToAccountId().getUserId().getUsername(), item.getTransactionAmount(),
                    item.getTransactionTimestamp(), item.getTransactionType(), item.getTransactionDetail(),
                    item.getTransactionNotes());
            foundFilterOutcome.add(filter);
        }
        // Sum Outcome
        Double sumOutcome = 0D;
        for (TransactionEntity item : foundOutcome) {
            sumOutcome += item.getTransactionAmount();
        }

        // All Transaction
        List<TransactionEntity> foundTransaction = repo.findAllTransactionByAccountId(foundAccount);
        List<TransactionItemDto> foundFilterAll = new ArrayList<>();
        for (TransactionEntity item : foundTransaction) {
            TransactionItemDto filter = new TransactionItemDto(item.getFromAccountId().getUserId().getUsername(),
                    item.getToAccountId().getUserId().getUsername(), item.getTransactionAmount(),
                    item.getTransactionTimestamp(), item.getTransactionType(), item.getTransactionDetail(),
                    item.getTransactionNotes());
            foundFilterAll.add(filter);
        }

        // Balance History
        List<?> foundBalance = repo.findTransactionBalanceHistory(foundAccount);

        // 2 transaction each Today and Week
        List<TransactionEntity> today2 = repo.findToday2Transaction(foundAccount);
        List<TransactionItemDto> foundToday2 = new ArrayList<>();
        for (TransactionEntity item : today2) {
            TransactionItemDto filter = new TransactionItemDto(item.getFromAccountId().getUserId().getUsername(),
                    item.getToAccountId().getUserId().getUsername(), item.getTransactionAmount(),
                    item.getTransactionTimestamp(), item.getTransactionType(), item.getTransactionDetail(),
                    item.getTransactionNotes());
            foundToday2.add(filter);
        }
        List<TransactionEntity> week2 = repo.findWeek2Transaction(foundAccount);
        List<TransactionItemDto> foundWeek2 = new ArrayList<>();
        for (TransactionEntity item : week2) {
            TransactionItemDto filter = new TransactionItemDto(item.getFromAccountId().getUserId().getUsername(),
                    item.getToAccountId().getUserId().getUsername(), item.getTransactionAmount(),
                    item.getTransactionTimestamp(), item.getTransactionType(), item.getTransactionDetail(),
                    item.getTransactionNotes());
            foundWeek2.add(filter);
        }

        List<TransactionEntity> month2 = repo.findMonth2Transaction(foundAccount);
        List<TransactionItemDto> foundMonth2 = new ArrayList<>();
        for (TransactionEntity item : month2) {
            TransactionItemDto filter = new TransactionItemDto(item.getFromAccountId().getUserId().getUsername(),
                    item.getToAccountId().getUserId().getUsername(), item.getTransactionAmount(),
                    item.getTransactionTimestamp(), item.getTransactionType(), item.getTransactionDetail(),
                    item.getTransactionNotes());
            foundMonth2.add(filter);
        }

        IncomeOutcomeDto data = new IncomeOutcomeDto(sumIncome, sumOutcome, foundFilterIncome, foundFilterOutcome,
                foundFilterAll, foundBalance, foundToday2, foundWeek2, foundMonth2);
        return data;
    }

    @Override
    public ResponseEntity<?> transactionTransfer(TransactionDto dto) throws ResourceNotFoundException {
        StatusMessageDto<TransactionEntity> response = new StatusMessageDto<>();
        Date date = new Date();
        Integer accountId = enc.decryptString(dto.getFromAccountId());

        Timestamp ts = new Timestamp(date.getTime());
        AccountEntity receiver = accountRepo.getAccountUserByUserId(dto.getToUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find user with this ID" + dto.getToUserId()));
        // AccountEntity receiver = accountRepo.findById(dto.getToAccountId())
        // .orElseThrow(() -> new ResourceNotFoundException("Receiver with this id is
        // not exist"));
        // --------- Will Change set as the active user who sends the money -----------
        AccountEntity sender = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender with this id is not exist"));
        UserDetailEntity senderData = userRepo.findById(sender.getUserId().getUserId())
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
        newTransaction.setToAccountBalance(receiver.getBalance() + dto.getTransactionAmount());
        newTransaction.setFromAccountBalance(sender.getBalance() - dto.getTransactionAmount());
        // Check balance
        if (sender.getBalance() <= dto.getTransactionAmount()) {
            return ResponseEntity.ok().body("Amount Exceeds Balance");
        }

        if (senderData.getPin().equals(dto.getUserPin())) {

            receiver.setBalance(receiver.getBalance() + dto.getTransactionAmount());

            sender.setBalance((sender.getBalance() - dto.getTransactionAmount()));
            // Try save
            try {
                newTransaction.setIsSuccess(0);
                repo.save(newTransaction);
                accountRepo.save(sender);
                accountRepo.save(receiver);
                response.setData(newTransaction);
                response.setStatus(HttpStatus.OK.toString());
                response.setMessage("Transfer Success");
                return ResponseEntity.ok().body(response);
            } catch (Exception e) {
                newTransaction.setIsSuccess(1);
                return ResponseEntity.ok().body(e.getMessage());
            }
        }
        // If not fulfilling all the condition
        response.setMessage("Sorry, It Seems You Put The Wrong Pin  : " + dto.getUserPin());
        response.setStatus(HttpStatus.OK.toString());
        return ResponseEntity.ok().body(response);
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
        newTransaction.setToAccountBalance(receiver.getBalance() + dto.getTransactionAmount());
        newTransaction.setFromAccountBalance(sender.getBalance() - dto.getTransactionAmount());
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
    public ResponseEntity<?> findTransactionBalanceHistory(Integer accountId) throws ResourceNotFoundException {
        StatusMessageDto response = new StatusMessageDto<>();
        AccountEntity foundAccount = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("cannot found history with this accountid"));
        try {
            response.setMessage("Success");
            response.setStatus(HttpStatus.OK.toString());
            response.setData(repo.findTransactionBalanceHistory(foundAccount));
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setMessage("Failed");
            response.setStatus(HttpStatus.EXPECTATION_FAILED.toString());
            return ResponseEntity.ok().body(response);
        }

    }

    @Override
    public ResponseEntity<?> findTransactionPerDay(Integer accountId) throws ResourceNotFoundException {
        StatusMessageDto<TransactionGraphDto> response = new StatusMessageDto<>();
        TransactionGraphDto graph = new TransactionGraphDto();
        AccountEntity foundAccount = accountRepo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found active user with this account id"));
        graph.setFirst(repo.findPerDay(foundAccount, 0).orElse(null));
        graph.setSecond(repo.findPerDay(foundAccount, 1).orElse(null));
        graph.setThird(repo.findPerDay(foundAccount, 2).orElse(null));
        graph.setForth(repo.findPerDay(foundAccount, 3).orElse(null));
        graph.setFifth(repo.findPerDay(foundAccount, 4).orElse(null));
        graph.setSixth(repo.findPerDay(foundAccount, 5).orElse(null));
        graph.setSeventh(repo.findPerDay(foundAccount, 6).orElse(null));
        try {
            response.setMessage("Success");
            response.setStatus(HttpStatus.OK.toString());
            response.setData(graph);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.setMessage("Failed");
            response.setStatus(HttpStatus.EXPECTATION_FAILED.toString());
            return ResponseEntity.ok().body(response);
        }

    }

}
