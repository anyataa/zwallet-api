package com.zwallet.zwalletapi.Service;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.AccountDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.AccountRepository;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Utils.Exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountImp implements AccountService {
    @Autowired
    AccountRepository repo;
    @Autowired
    UserDetailRepository userRepo;

    @Override
    public AccountEntity getAccountById(Integer accountId) throws ResourceNotFoundException {
        AccountEntity foundAccount = repo.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("cannot find account with id : " + accountId));
        return foundAccount;
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
        AccountEntity newAccount = new AccountEntity();
        UserDetailEntity newUser = dto.getUser();
        dto.setData(0D, newAccount, newUser);
        userRepo.save(newUser);
        newAccount.setUserId(newUser);
        repo.save(newAccount);
        return ResponseEntity.ok().body(newAccount);

    }

    @Override
    public ResponseEntity<?> putAccount(Integer accountId, Integer userId, Double balance) {
        AccountEntity foundAccount = repo.findById(accountId).orElse(null);
        if (userRepo.findById(userId) != null) {
            foundAccount.setUserId(userRepo.findById(userId).orElse(null));
            foundAccount.setBalance(balance);
        }
        repo.save(foundAccount);
        return ResponseEntity.ok().body("Add user id to account success");
    }

    @Override
    public AccountEntity getAccountByUserName(String username) throws ResourceNotFoundException {
        AccountEntity foundAccount = repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find account with username : " + username));
        return foundAccount;
    }

}
