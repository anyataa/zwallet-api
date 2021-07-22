package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Utils.ExceptionNotFound;

import org.springframework.http.ResponseEntity;

public interface AccountService {

    // Called on he dashboard to show balance
    public Optional<AccountEntity> getAccountById(Integer accountId);

    // Called when transaction happen
    public void updateBalance();

    // Debug Purpos
    public List<AccountEntity> getAllAccount() throws ExceptionNotFound;

    // Post Purpose during user initiation
    public ResponseEntity<?> postAccount(AccountDto dto);

}
