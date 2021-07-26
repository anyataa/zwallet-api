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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountImp implements AccountService {
    @Autowired
    AccountRepository repo;
    @Autowired
    UserDetailRepository userRepo;

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
        AccountEntity newAccount = new AccountEntity();
        UserDetailEntity newUser = new UserDetailEntity(dto.getUser().getUsername(), dto.getUser().getEmail(),
                dto.getUser().getPassword(), dto.getUser().getPin(), dto.getUser().getUserFname(),
                dto.getUser().getUserLname(), dto.getUser().getUserImage(), dto.getUser().getBankNumber(), "USER");

        dto.setData(dto.getBalance(), newAccount, newUser);
        try {
            userRepo.save(newUser);
            newAccount.setUserId(newUser);
            repo.save(newAccount);
            return ResponseEntity.ok().body(newAccount);
        } catch (Exception e) {
            return ResponseEntity.ok().body(newAccount + " failed to save");
        }
    }

    @Override
    public ResponseEntity<?> putAccount(Integer accountId, Integer userId) {
        AccountEntity foundAccount = repo.findById(accountId).orElse(null);
        if (userRepo.findById(userId) != null) {
            foundAccount.setUserId(userRepo.findById(userId).orElse(null));
        }
        repo.save(foundAccount);
        return ResponseEntity.ok().body("Add user id to account success");
    }

}
