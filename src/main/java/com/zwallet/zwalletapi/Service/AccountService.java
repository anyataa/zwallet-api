package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Utils.ExceptionNotFound;

import org.springframework.http.ResponseEntity;

public interface AccountService {

    // Called on he dashboard to show balance
    Optional<AccountEntity> getAccountById(Integer accountId);

    // Called when transaction happen
    void updateBalance();

    // Debug Purpos
    List<AccountEntity> getAllAccount() throws ExceptionNotFound;

    // Post Purpose during user initiation
    ResponseEntity<?> postAccount(AccountDto dto);

}
