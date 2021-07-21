package com.zwallet.zwalletapi.Model.Dto;

import com.zwallet.zwalletapi.Model.Entity.AccountEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDto {

    private Double balance;

    public void setData(Double balance, AccountEntity account) {
        account.setBalance(balance);
    }
}
