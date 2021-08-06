package com.zwallet.zwalletapi.Model.Dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.zwallet.zwalletapi.Model.Entity.AccountEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionDto {

    private Double transactionAmount;

    private String transactionNotes;

    private Timestamp transactionTimestamp;

    // 0 : Debit | 1 : Credit

    private Integer transactionType;

    // 1 : Transfer | 2 : Subscription | 3 : Payment | 4 : Top Up | 5 : Retrieve

    private Integer transactionDetail;

    private Integer isSuccess;

    // Sender | Type Transaction : 0 Debit

    private Integer fromAccountId;

    // Receiver | Type Transaction : 1 Credit

    private Integer toAccountId;

    private String username;

    private Integer toUserId;

    private Double fromAccountBalance;

    private Double toAccountBalance;

    private String userPin;

}
