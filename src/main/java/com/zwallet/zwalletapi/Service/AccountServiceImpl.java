package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Utils.ExceptionNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repo;

    @Override
    public Optional<AccountEntity> getAccountById(Integer accountId) {
        return repo.findById(accountId);
    }

    @Override
    public void updateBalance() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<AccountEntity> getAllAccount() {

        return null;
    }

    @Override
    public ResponseEntity<?> postAccount(AccountDto dto) {
        try {
            AccountEntity newAccount = new AccountEntity();
            dto.setData(dto.getBalance(), newAccount);
            repo.save(newAccount);
        } catch (Exception e) {

        }
        return null;
    }

}
