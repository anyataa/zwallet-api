package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.http.ResponseEntity;

public class AccountImp implements AccountService {

    @Override
    public Optional<AccountEntity> getAccountById(Integer accountId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateBalance() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<AccountEntity> getAllAccount() throws ResourceNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> postAccount(AccountDto dto) {
        // TODO Auto-generated method stub
        return null;
    }

}
