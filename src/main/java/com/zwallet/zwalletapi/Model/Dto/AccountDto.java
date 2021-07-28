package com.zwallet.zwalletapi.Model.Dto;

import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
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
