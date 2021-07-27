package com.zwallet.zwalletapi.Model.Dto;

import java.sql.Timestamp;

import com.zwallet.zwalletapi.Model.Entity.AccountEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VendorDto {
    private Double transactionAmount;
    private AccountEntity fromAccount;
    private AccountEntity toAccount;
    private Timestamp date;

}
