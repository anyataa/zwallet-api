package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.IncomeOutcomeDto;
import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;

public interface TransactionService {

    IncomeOutcomeDto getAllTransaction(Integer accountId) throws ResourceNotFoundException;;

    TransactionEntity getBySenderId(Integer Idsender) throws ResourceNotFoundException;

    ResponseEntity<?> transactionTransfer(TransactionDto dto) throws ResourceNotFoundException;
    // sum outcome

    // Integer sumOutcome(Integer)

}
