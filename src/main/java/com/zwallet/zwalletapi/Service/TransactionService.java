package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.IncomeOutcomeDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionBalanceHistoryDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionPeriodFilterDto;
import com.zwallet.zwalletapi.Model.Dto.VendorDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;

public interface TransactionService {

    IncomeOutcomeDto getAllTransaction(Integer accountId) throws ResourceNotFoundException;;

    // TransactionEntity getBySenderId(Integer Idsender) throws
    // ResourceNotFoundException;

    ResponseEntity<?> transactionTransfer(TransactionDto dto) throws ResourceNotFoundException;

    ResponseEntity<?> vendorTransfer(Integer transactionType, Integer transactionDetail, TransactionDto dto,
            AccountEntity receiver, AccountEntity sender) throws ResourceNotFoundException;

    TransactionPeriodFilterDto getTransactionPeriodically(Integer accountId) throws ResourceNotFoundException;

    ResponseEntity<?> findTransactionBalanceHistory(Integer accountId) throws ResourceNotFoundException;
}
