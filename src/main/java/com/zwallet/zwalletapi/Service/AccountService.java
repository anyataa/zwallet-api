package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;

public interface AccountService {

    // Called on he dashboard to show balance
    AccountEntity getAccountById(Integer accountId) throws ResourceNotFoundException;

    // Called when transaction happen
    void updateBalance();

    // Debug Purpos

    List<AccountEntity> getAllAccount() throws ResourceNotFoundException;

    // Post Purpose during user initiation
    ResponseEntity<?> postAccount(AccountDto dto);

    // put
    ResponseEntity<?> putAccount(Integer accountId, Integer userId, Double balance);

    ResponseEntity<?> putAccountBalance(Integer accountId, Double balance) throws ResourceNotFoundException;

    AccountEntity getAccountByUserName(String username) throws ResourceNotFoundException;

}
