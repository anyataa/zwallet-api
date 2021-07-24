package com.zwallet.zwalletapi.Service;

import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.TransactionDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;

public interface TransactionService {

    Optional<TransactionEntity> getByReceiverId(AccountEntity receiver) throws ResourceNotFoundException;;

    Optional<TransactionEntity> getBySenderId(AccountEntity sender) throws ResourceNotFoundException;

    ResponseEntity<?> transactionTransfer(TransactionDto dto) throws ResourceNotFoundException;

}
