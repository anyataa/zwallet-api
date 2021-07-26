package com.zwallet.zwalletapi.Model.Dto;

import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDto {

    private Double balance;
    private UserDetailEntity user;

    public void setData(Double balance, AccountEntity account, UserDetailEntity user) {
        account.setBalance(balance);
    }
}
